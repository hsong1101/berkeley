from skimage.io import imread, imsave
import matplotlib.pyplot as plt
import numpy as np
import os
import warnings

def read_rich_labels(path):
	"""
	Checks the structure of your rich_labels.txt file.
	Returns a dictionary:
	
	key: file name
	value: a tuple of floats  (<latitude>, <longitude>)
	"""
	location_dict = {}
	with open(os.path.join(path,'rich_labels.txt')) as f:
		content = f.readlines()
	for line in content:
		linecontent = line.split()

		# make sure each line is structured as follows:<image name> <latitude> <longitude>
		assert len(linecontent) >= 3, "Unexpectedly short line in rich_labels.txt: " + line
		if len(linecontent) > 3: 
			warnings.warn('Unexpected line in rich_labels.txt: ' + line + 
			  			  '\n Using first three words: ' + str(linecontent), stacklevel=0)
		try:
			location_dict[linecontent[0]] = (float(linecontent[1]),float(linecontent[2]))

			# make sure you have latitude and longitude coordinates are not flipped
			# assuming that images are from North America
			assert float(linecontent[1]) <= float(linecontent[2])

		except ValueError as e:
			warnings.warn("Unexpected lat/long in rich_labels.txt: " + 
						  str(linecontent[1:3]), stacklevel=0)
	return location_dict

def read_image_files(path):
	"""
	Returns a dictionary:

	key: file name (string)
	value: (256, 256, 3) array (integer)
	"""
	img_dict = {}
	for f in os.listdir(path):
		ext = os.path.splitext(f)[1]
		
		assert ext.lower() != '.jpg', "Make sure you do not save the pictures as .jpg files,"
		if ext.lower() != '.png':
			continue

		img_dict[f] = imread(os.path.join(path,f))

		# make sure that you have cropped and/or resized your images before this step
		if img_dict[f].shape != (256,256,3):
			warnings.warn("Unexpected image size: " + str(img_dict[f].shape),stacklevel=0)
	return img_dict

def plot_20_images(img_dict, title_dict):
	"""
	Plot the resized images.
	"""
	plt.figure(figsize=(8,8))
	filelist = list(img_dict.keys())
	for i in range(20):
		plt.subplot(4,5,i+1)
		if i < len(filelist):
			plt.imshow(img_dict[filelist[i]])
			try:
				plt.title(title_dict[filelist[i]])
			except KeyError as e:
				warnings.warn("Key missing from title dictionary for "+filelist[i],stacklevel=0)
	plt.tight_layout()


acc_imgs = read_image_files('./data/acc/')
acc_locs = read_rich_labels('./data/acc/')
plot_20_images(acc_imgs, acc_locs)

inacc_imgs = read_image_files('./data/inacc/')
inacc_locs = read_rich_labels('./data/inacc/')
plot_20_images(inacc_imgs, inacc_locs)

plt.show()
