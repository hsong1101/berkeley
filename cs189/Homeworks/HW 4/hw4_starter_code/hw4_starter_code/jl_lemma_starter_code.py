import numpy as np
import matplotlib.pyplot as plt
# %matplotlib inline
import sklearn.linear_model
from sklearn.model_selection import train_test_split


######## EMBEDDING FUNCTIONS ##########
######## YOUR CODE FOR (c) ##########


## Input: original dimension d,
## Input: embedding dimension k
## Output: d x k random
## Gaussian matrix J with entry-wise
## variances 1/k so that, 
## for any row vector z^T in R^d, 
## z^T J  is a random features embedding for z^T
def random_JL_matrix(d, k):
    #your code here


## Input: n x d data matrix X
## Input: embedding dimension k
## Output: d x k matrix V
## with orthonormal columns 
## corresponding to the top k right signular vectors 
## of X. Thus, for a row vector z^T in R^d
## z^T V  is the projection of z^T 
## onto the the top k right-singular vectors of X,
def pca_embedding_matrix(X, k):
    #your code here



######## END YOUR CODE FOR (c) ##########



# applies the linear transformation N 
# to the rows of X, via X.dot(N)
def linear_feature_transform(X, N):
    return X.dot(N)

# uses pca_embedding_matrix method
# to embed the rows of X onto the first
# k principle components
def pca_embedding(X,k):
    P = pca_embedding_matrix(X, k)
    return linear_feature_transform(X,P)

# uses random_JL_matrix method
# to transform rows of X
# by a k-dimensional JL transform
def random_embeddings(X,k):
    P = random_JL_matrix(X, k)
    return linear_feature_transform(X,J)


######### LINEAR MODEL FITTING ############
######### DO NOT ALTER ##################
def rand_embed_accuracy_split(X, y, k):
    '''
    Fitting a k dimensional feature set obtained
    from random embedding of X, versus y
    for binary classification for y in {-1, 1}
    '''
    
    # test train split
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.20, random_state=42)
    
    # random embedding
    _, d = X.shape
    J = random_JL_matrix(d,k)
    rand_embed_X = linear_feature_transform(X_train, J)
    
    # fit a linear model
    line = sklearn.linear_model.LinearRegression(fit_intercept=False)
    line.fit(rand_embed_X, y_train)
    
    # predict y
    y_pred=line.predict(linear_feature_transform(X_test, J))
    
    # return the test error
    return 1-np.mean(np.sign(y_pred)!= y_test)

def pca_embed_accuracy_split(X, y, k):
    '''
    Fitting a k dimensional feature set obtained
    from PCA embedding of X, versus y
    for binary classification for y in {-1, 1}
    '''

    # test-train split
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.20)

    # pca embedding
    V = pca_embedding_matrix(X_train, k)
    pca_embed_X = linear_feature_transform(X_train,V)
                
    # fit a linear model
    line = sklearn.linear_model.LinearRegression(fit_intercept=False)
    line.fit(pca_embed_X, y_train)
    
     # predict y
    y_pred=line.predict(linear_feature_transform(X_test,V))
    

    # return the test error
    return 1-np.mean(np.sign(y_pred)!= y_test)


######## LOADING THE DATASETS #########

# to load the data:
# data = np.load('data/data1.npz')
# X = data['X']
# y = data['y']
# n, d = X.shape


# n_trials = 10  # to average for accuracies over random embeddings

######### YOUR CODE GOES HERE ##########

# Using PCA and Random embedding for:
# Visualizing the datasets 





# Computing the accuracies over different datasets.




# Don't forget to average the accuracy for multiple
# random embeddings to get a smooth curve.





# And computing the SVD of the feature matrix



######## YOU CAN PLOT THE RESULTS HERE ########

# plt.plot, plt.scatter would be useful for plotting

