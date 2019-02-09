from skimage.io import imread, imsave
from skimage.transform import resize
from skimage.util import crop
import matplotlib.pyplot as plt
import numpy as np


def crop_size(image_path, save_path):
	"""
	The function takes in the path to an image and resizes it to be 256 by 256 pixels.
	"""

	# Loading image file
	image = imread(image_path)

	# Cropping image to be square
	## Determining extra pixels
	extra = np.abs(image.shape[0] - image.shape[1])
	removal = (np.floor(extra/2), np.ceil(extra/2))

	## Determining if width or height should be cropped
	if image.shape[0] >= image.shape[1]:
		cropped_image = crop(image,(removal,(0,0),(0,0)))
	else: 
		cropped_image = crop(image,((0,0),removal,(0,0)))

	# Resizing image to be 256x256
	resized_image = resize(cropped_image, (256,256,3))

	plt.figure(figsize=(5,5))
	plt.imshow(resized_image)
	plt.show()

	imsave(save_path, resized_image)

	return


# Setting image path and save path
image_path = './house.png'
save_path = './data/inacc/house_processed.png'

crop_size(image_path, save_path)
