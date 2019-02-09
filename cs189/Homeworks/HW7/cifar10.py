#!/usr/bin/env python

"""
    cifar10.py
"""
import sys
import argparse
import numpy as np
import matplotlib.pyplot as plt

import torch
from torch import nn
from torch.nn import functional as F
import torchvision
from torchvision import transforms, datasets
import torch.optim as optim
import pandas as pd

from sklearn import linear_model
from sklearn.decomposition import PCA
from sklearn.cross_decomposition import CCA

# --
# CLI
# The default values of most arguments are sufficient for this problem
# You may want to use --subset-train or --subset-val to use only a subset
# of the data while testing your code.
# You may want to use --data-dir to save the CIFAR-10 dataset in a
# specific location
def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument('--epochs', type=int, default=1)
    parser.add_argument('--batch-size', type=int, default=128)
    parser.add_argument('--subset-train', type=int, default=None)
    parser.add_argument('--subset-val', type=int, default=None)
    parser.add_argument('--gpu', default=False, action="store_const", const=True)
    parser.add_argument('--seed', type=int, default=123)
    parser.add_argument('--data-dir', default='./data')
    return parser.parse_args()
args = parse_args()

# --
# Loading data
def load_data():
    print('cifar10.py: making dataloaders...', file=sys.stderr) 
    # transforms define preprocessing on the dataset   
    transform_train = transforms.Compose([
        # TO IMPLEMENT Part (d) random crop
        # TO IMPLEMENT Part (d) random horizontal flip
        transforms.ToTensor(),
        transforms.Normalize(mean=(0.4914, 0.4822, 0.4465), std= (0.24705882352941178, 0.24352941176470588, 0.2615686274509804))
    ])
    transform_val = transforms.Compose([
        transforms.ToTensor(),
        transforms.Normalize(mean=(0.4914, 0.4822, 0.4465), std= (0.24705882352941178, 0.24352941176470588, 0.2615686274509804))
    ])
    # loading the dataset
    try:
        trainset = datasets.CIFAR10(root=args.data_dir, train=True, download=True, transform=transform_train)
        valset  = datasets.CIFAR10(root=args.data_dir, train=False, download=True, transform=transform_val)
    except:
        raise Exception('cifar10.py: error loading data -- try rerunning w/ `--download` flag')
    # selecting a random subset of the dataset if requested
    np.random.seed(args.seed)
    if (args.subset_train is not None):
        idxs = np.random.choice(len(trainset), args.subset_train, replace=False)
        trainset = torch.utils.data.Subset(trainset, idxs)
    if (args.subset_val is not None):
        idxs = np.random.choice(len(valset), args.subset_val, replace=False)
        trainset = torch.utils.data.Subset(valset, idxs)
    # data loading objects
    trainloader = torch.utils.data.DataLoader(
        trainset,
        batch_size=args.batch_size,
        shuffle=True,
        pin_memory=True,
    )
    valloader = torch.utils.data.DataLoader(
        valset,
        batch_size=512,
        shuffle=False,
        pin_memory=True,
    )
    dataloaders = {
        "train" : trainloader,
        "val"  : valloader,
    }
    return dataloaders

dataloaders = load_data()
classes = ('plane', 'car', 'bird', 'cat',
            'deer', 'dog', 'frog', 'horse', 'ship', 'truck')

# -- 
# Linear model methods
def get_X_Y(loader):
    X = []; Y = []
    for data in loader:
        for x in data[0]:
            X.append(x.numpy())
        for y in data[1]:
            Y.append(y.numpy())
    X = np.array(X); Y = np.array(Y)
    return X, Y

def get_PCA_embedding(X, k=2):
    pca = PCA(n_components=k)
    pca.fit(X)
    X_p = pca.transform(X)
    return X_p

def get_CCA_embedding(X, Y, k=2):
    cca = CCA(n_components=k)
    cca.fit(X, Y)
    X_c, Y_c = cca.transform(X, Y)
    return X_c, Y_c

# --
# Model definition
# Derived from models in `https://github.com/kuangliu/pytorch-cifar`
class PreActBlock(nn.Module):

    def __init__(self, in_channels, out_channels, stride=1):
        super().__init__()

        self.bn1   = nn.BatchNorm2d(in_channels)
        self.conv1 = nn.Conv2d(in_channels, out_channels, kernel_size=3, stride=stride, padding=1, bias=False)
        self.bn2   = nn.BatchNorm2d(out_channels)
        self.conv2 = nn.Conv2d(out_channels, out_channels, kernel_size=3, stride=1, padding=1, bias=False)

        if stride != 1 or in_channels != out_channels:
            self.shortcut = nn.Sequential(
                nn.Conv2d(in_channels, out_channels, kernel_size=1, stride=stride, bias=False)
            )

    def forward(self, x):
        out = F.relu(self.bn1(x))
        shortcut = self.shortcut(out) if hasattr(self, 'shortcut') else x
        out = self.conv1(out)
        out = self.conv2(F.relu(self.bn2(out)))
        return out + shortcut


class ResNet18(nn.Module):
    def __init__(self, num_blocks=[2, 2, 2, 2], num_classes=10):
        super().__init__()
        self.in_channels = 64

        self.prep = nn.Sequential(
            nn.Conv2d(3, 64, kernel_size=3, stride=1, padding=1, bias=False),
            nn.BatchNorm2d(64),
            nn.ReLU()
        )

        self.layers = nn.Sequential(
            self._make_layer(64, 64, num_blocks[0], stride=1),
            self._make_layer(64, 128, num_blocks[1], stride=2),
            self._make_layer(128, 256, num_blocks[2], stride=2),
            self._make_layer(256, 256, num_blocks[3], stride=2),
        )

        self.classifier = nn.Linear(512, num_classes)

    def _make_layer(self, in_channels, out_channels, num_blocks, stride):

        strides = [stride] + [1] * (num_blocks-1)
        layers = []
        for stride in strides:
            layers.append(PreActBlock(in_channels=in_channels, out_channels=out_channels, stride=stride))
            in_channels = out_channels

        return nn.Sequential(*layers)

    def forward(self, x):
        x = x.float()
        x = self.prep(x)
        x = self.layers(x)

        x_avg = F.adaptive_avg_pool2d(x, (1, 1))
        x_avg = x_avg.view(x_avg.size(0), -1)

        x_max = F.adaptive_max_pool2d(x, (1, 1))
        x_max = x_max.view(x_max.size(0), -1)

        x = torch.cat([x_avg, x_max], dim=-1)

        x = self.classifier(x)

        return x

# --
# Learning rate functions
def set_lr(lr, optimizer):
    for param_group in optimizer.param_groups:
        param_group['lr'] = lr

def const_lr_maker(const=0.001):
    # TO IMPLEMENT Part (e)

    return f

def anneal_lr_maker(factor=0.99):
    # TO IMPLEMENT Part (e)

    return f

def special_lr_maker(hp_max=0.1, epochs=3, hp_init=0.0, hp_final=0.005, extra=5):
    def f(curr_lr, epoch, batch_idx, N):
        progress = epoch + batch_idx / N
        if progress < epochs / 2:
            return 2 * hp_max * (1 - (epochs - progress) / epochs)
        elif progress <= epochs:
            return hp_final + 2 * (hp_max - hp_final) * (epochs - progress) / epochs
        elif progress <= epochs + extra:
            return hp_final * (extra - (progress - epochs)) / extra
        else:
            return hp_final / 10
    return f

const_lr = const_lr_maker()
anneal_lr = anneal_lr_maker()
special_lr = special_lr_maker(epochs=args.epochs+3)

# --
# Model training
def train_with_lr_scheme(calc_lr):
    
    print('cifar10.py: initializing model...', file=sys.stderr)
    if (torch.cuda.is_available() and args.gpu):
        device = torch.device('cuda')
    else:
        device = torch.device('cpu')

    model = ResNet18().to(device)
    model.verbose = True
    # --
    # Initialize optimizer
    print('cifar10.py: training...', file=sys.stderr)
    criterion = nn.CrossEntropyLoss()
    optimizer = optim.SGD(model.parameters(), lr=0.001, momentum=0.9)
    lr = 0.01

    trainloader, valloader = dataloaders['train'], dataloaders['val']
    N = len(trainloader)
    iter_count = 0 # a count of all iterations
    for epoch in range(args.epochs):  # loop over the dataset multiple times
        running_loss = 0.0
        total_train = 0; correct_train = 0
        total_val = 0; correct_val = 0
        for i, data in enumerate(trainloader, 0):
            # get the inputs
            inputs, labels = data
            # zero the parameter gradients
            optimizer.zero_grad()
            
            if (torch.cuda.is_available() and args.gpu):
                labels = labels.cuda()
                inputs = inputs.cuda()
            # forward + backward
            outputs = model(inputs)
            loss = criterion(outputs, labels)
            loss.backward()

            # optimize
            lr = calc_lr(lr, epoch, i, N)
            set_lr(lr, optimizer)
            iter_count += 1
            optimizer.step()

            # compute training statistics
            logits = outputs.cpu().detach().numpy()
            y_pred_train = np.argmax(logits, axis=1)
            y_train = labels.cpu().detach().numpy()
            total_train += y_train.shape[0]
            correct_train += sum(y_pred_train == y_train)
            running_loss += loss.item()
            print("Epoch:", epoch, "\tMiniBatch:", i, "\tPartial Training Accuracy:", correct_train/total_train,  "\tRunning Loss:", running_loss/(i+1))
        print("Epoch:", epoch, "\tFinal Training Accuracy:", {correct_train/total_train})
        for i, data in enumerate(valloader, 0):
            # get the inputs
            inputs, labels = data
            if (torch.cuda.is_available() and args.gpu):
                labels = labels.cuda()
                inputs = inputs.cuda()
            # predict outputs
            outputs = model(inputs)
            logits = outputs.cpu().detach().numpy()

            # compute validation statistics
            y_pred_train = np.argmax(logits, axis=1)
            y_train = labels.cpu().detach().numpy()
            total_val += y_train.shape[0]
            correct_val  += sum(y_pred_train == y_train)
            print("Epoch:", epoch, "\tMiniBatch:", i, "\tPartial Validation Accuracy:", correct_val/total_val)
        print("Epoch:", epoch, "\tFinal Validation Accuracy:", {correct_val/total_val})
        result = {}
        result['train_accuracy'] = correct_train/total_train
        result['val_accuracy'] = correct_val/total_val
        result['num_epochs'] = args.epochs
        result['train_loss'] = running_loss 
    return result

VISUALIZATION_FLAG = False # Part (a)
LINEAR_FLAG = False # Part (b)
PRINT_NN_FLAG = False # Part (c)
TRAIN_NN_FLAG = False # Parts (d-f)

if VISUALIZATION_FLAG:
    print("===== Constructing data matrices ====")
    X, labels = get_X_Y(dataloaders['train'])
    X_val, labels_val = get_X_Y(dataloaders['val'])

    # vectorizing images in X
    X = X.reshape(X.shape[0],-1)
    X_val = X_val.reshape(X_val.shape[0],-1)

    # changing Y to one-hot encoding
    Y = np.eye(10)[labels]

    # PCA embedding
    print("===== Computing PCA Embedding ====")
    Xp = get_PCA_embedding(X)

    # CCA embedding 
    print("===== Computing CCA Embedding ====")
    Xc, _ = get_CCA_embedding(X, Y)

    # Plotting visualization
    # TO IMPLEMENT Part (a)
    
if LINEAR_FLAG:
    print("===== Constructing data matrices ====")
    X, labels = get_X_Y(dataloaders['train'])
    X_val, labels_val = get_X_Y(dataloaders['val'])

    # vectorizing images in X
    X = X.reshape(X.shape[0],-1)
    X_val = X_val.reshape(X_val.shape[0],-1)

    # changing Y to one-hot encoding
    Y = np.eye(10)[labels]

    # Simple linear classifier on raw data
    print("===== Fitting linear model ====")
    lin = linear_model.LinearRegression().fit(X, Y)

    # test accuracy
    Y_pred = lin.predict(X) # train predictions
    labels_pred = np.argmax(Y_pred, axis=1) # converting one-hot prediction to label
    train_accuracy = np.sum(labels_pred == labels) / len(labels)

    # validation accuracy
    Y_pred_val = lin.predict(X_val) # validation predictions
    labels_pred_val = np.argmax(Y_pred_val, axis=1) # converting one-hot prediction to label
    val_accuracy = np.sum(labels_pred_val == labels_val) / len(labels_val)

    print("===== Linear Model Results ====")
    print('train accuracy:\t',train_accuracy)
    print('validation accuracy:\t',val_accuracy)
if PRINT_NN_FLAG:
    model = ResNet18().to(torch.device('cpu'))
    model.verbose = True
    print(model)
if TRAIN_NN_FLAG: 
    print("===== Training network with a constant LR scheme ====")
    result_const = train_with_lr_scheme(const_lr)
    result_const['lr_scheme'] = 'const'
    print("===== Training network with an annealing LR scheme ====")
    result_anneal = train_with_lr_scheme(anneal_lr)
    result_anneal['lr_scheme'] = 'anneal'
    print("===== Training network with a special LR scheme ====")
    result_special = train_with_lr_scheme(special_lr)
    result_special['lr_scheme'] = 'special_0'
    print("==== Summary ===")
    df = pd.DataFrame([result_const, result_anneal, result_special])
    print(df)