
# Intro

Before starting, I would like to show you that if there exists any dependency between features (columns), there cannot be an inverse matrix that we cannot use closed-form (or normal equation) solution for OLS or Ridge.

## Load libraries


```python
# First load the necessary libraries
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
```

Now suppose we have declared a matrix as follows


```python
mat = np.matrix([[1,2],[2,4]])
```

Now we check this.


```python
mat
```




    matrix([[1, 2],
            [2, 4]])



As you can see above, the first column and second are linearly dependent that 2nd column is twice big as the first. 
It is easy to see that the basis for this matrix is just the 1st column.
So if we try to get the inverse matrix using numpy,


```python
np.linalg.inv(mat)
```


    ---------------------------------------------------------------------------

    LinAlgError                               Traceback (most recent call last)

    <ipython-input-4-4f16ae3668f6> in <module>()
    ----> 1 np.linalg.inv(mat)
    

    c:\users\hsong1101\anaconda3\envs\tensorflow\lib\site-packages\numpy\linalg\linalg.py in inv(a)
        526     signature = 'D->D' if isComplexType(t) else 'd->d'
        527     extobj = get_linalg_error_extobj(_raise_linalgerror_singular)
    --> 528     ainv = _umath_linalg.inv(a, signature=signature, extobj=extobj)
        529     return wrap(ainv.astype(result_t, copy=False))
        530 
    

    c:\users\hsong1101\anaconda3\envs\tensorflow\lib\site-packages\numpy\linalg\linalg.py in _raise_linalgerror_singular(err, flag)
         87 
         88 def _raise_linalgerror_singular(err, flag):
    ---> 89     raise LinAlgError("Singular matrix")
         90 
         91 def _raise_linalgerror_nonposdef(err, flag):
    

    LinAlgError: Singular matrix


We will get the above error. I won't get into details as to why there cannot exist inverse matrix when a matrix is linearly dependent since it should be basic knowledge. 
If you don't know why, for now just assume that it just cannot and try to look it up on Google since a lot of simple explanations are out there.

# Coding

For the coding part, I'm going to use the housing price dataset from kaggle.
You can get the file at [this page](https://www.kaggle.com/c/house-prices-advanced-regression-techniques/data)<br>
(If you don't have an account, you may have to create one to download)

## Load the data

Place the data file in the same repository in which you store the python (or jupyter) file, so it's easy to navigate.


```python
dat = pd.read_csv('./data/train.csv')
```

Let's look at the data.


```python
dat.head()
```


```python
dat.shape
```

As seen by shape, the data has 1460 rows and 81 features which some have NaN values which represents as Not a Number.
As this post is about showing how to code for linear regression problems, I will only do necessary data cleaning and ignore the rest. 
To make it simple, let's first see which features we could use.


```python
list(dat)
```

## Data Cleaning

The above list is the names of the features. Let's see which ones are integer or float (numberic) types.


```python
dat.select_dtypes(include=['number']).head()
```

We reduced from 81 features to 38 but still quite many to show simple linear regression. So let's drop any columns with 0 or null values in it. But before that, to avoid repeating the same selection, let's redeclare the dataframe.


```python
dat = dat.select_dtypes(include=['number'])
```


```python
dat.shape
```

Now we have new dataframe, let's split the price column from all others.


```python
dat = dat.dropna(axis=0)
```


```python
price = dat[['SalePrice']]
dat = dat.drop('SalePrice', axis=1)
```


```python
dat.isnull().any()
```

Out of the features we have, we will only LotArea to make this simple.


```python
dat = dat[['LotArea']]
```


```python
dat.head()
```

Make sure that we have the same number of data of dat as the price


```python
price.shape[0] == dat.shape[0]
```

## Building Model

There are two most popular libraries we could use for the regression. One is scikit and the other is tensorflow. 
In this post, I will use sklearn as it is simpler than tensorflow regression model. Also let's try implementing our own function and check which performs better.<br>
First let's go with scikit


```python
from sklearn.linear_model import LinearRegression
```

When we build a model, we would first like to split the data into training set, validation set, and test set but let's skip the validation for now. I'll cover the validation in another post.


```python
from sklearn.model_selection import train_test_split
```


```python
X_train, X_test, y_train, y_test = train_test_split(dat, price)
```


```python
X_train.head()
```


```python
y_train.head()
```

Test data is split to 75% of training and 25% of testing.<br>
Next thing we should do is simply declare a model and fit to the data. You can think of fitting as the finding a function that matches the most data points as explained in the concept part. 


```python
model = LinearRegression()
model.fit(X_train, y_train)
```




    LinearRegression(copy_X=True, fit_intercept=True, n_jobs=1, normalize=False)



Just two lines of code are enough for this. Now we predict the prices of testing sets and compare it with acutal data.


```python
pred = model.predict(X_test)
```

The first 5 values of the prediction are


```python
pred[:5]
```

Now that we both have prediction values and actual values, let's compare it.<br>
We will use <b>Mean Squared Error(MSE)</b> to compute the error and the formula for this is $$1/N \sum_i^N(y-\hat{y})^2$$ <br>
The only thing that is different from the OLS we used in the previous is that we are dividing the sum value by total number of datasets. 


```python
from sklearn.metrics import mean_squared_error
```

Let's try using the method from sklearn.metrics and implement on our own.


```python
mean_squared_error(pred, y_test)
```


```python
def mse(y, y_hat):
    return np.mean(np.square(y-y_hat))[0]
```


```python
mse(y_test, pred)
```

As you can see, both return the same value. But this value is too large that we cannot interpret and understand well. To fight this, we will use R2 metrics from the sklearn. This R2 is also called the coefficient of determination and is good to check whether the model is working as it should be or not. Its value can range from negative to 1 (but in rare times, it could be higher). 1 is the value we should be aiming for.


```python
from sklearn.metrics import r2_score
```


```python
np.round(r2_score(y_test, pred), 3)
```




    0.083



As we can see, our score is 0.251 which is really low. Our goal is to make the value as high as possible by feature-engineering which will be introducted in later post. For now, just see how the linear regression works. Let's see this in the graph.


```python
# Plot for Training sets
plt.scatter(X_train, y_train,color='g')
plt.plot(X_train, model.predict(X_train),color='k')
plt.show()

# Plot for Test sets
plt.scatter(X_test, y_test,color='r')
plt.plot(X_test, model.predict(X_test),color='b')
plt.show()
```


![png](Linear_files/Linear_60_0.png)



![png](Linear_files/Linear_60_1.png)


In above graphs, we see one data point that is far from the other data points which we call outlier. This is one of things that cause our model to work not so well as it can drastically affect the function. Removing outliers can improve our models.

Let's first inspect the data


```python
dat.describe()
```

We can see the while the mean is only 10122.95 and std is only 8129, the maximum value it has is 215245 which is much bigger than the rest of them as above graph. We will set the range with standard deviation.<br>
In normal distribution, 2 standard deviations away from the mean cover around 99.7% of the data. In this data, the lowest point or the min is 1300 which is much lower than 1 std so we will only put an upper boundary.

## Our Implementation

Let's load up another data and use it.


```python
dat2 = pd.read_csv('./data/train.csv')
```


```python
dat2 = dat2[['LotArea', 'SalePrice']]

dat2 = dat2[dat2 < np.std(dat2)*2 + np.mean(dat2)].dropna()

price2 = dat2[['SalePrice']]
dat2 = dat2[['LotArea']]
```


```python
dat2.describe()
```

Now the data seems fine to use the regression. With this new data we have, let's check the r2 score and compare it with the previous model score.


```python
W_train, W_test, z_train, z_test = train_test_split(dat2, price2)
```


```python
W_train.describe()
```

Now that we have dropped outliers, let's try the whole thing again.


```python
model.fit(W_train, z_train)
```




    LinearRegression(copy_X=True, fit_intercept=True, n_jobs=1, normalize=False)




```python
print('R2 Score : {}'.format(np.round(r2_score(z_test, model.predict(W_test)), 3)))
print('MSE : {}'.format(mean_squared_error(z_test, model.predict(W_test))))
```

    R2 Score : 0.097
    MSE : 3000658479.9397545
    

The r2 score increased and although it is very little, the model works better.


```python
# Plot for Training sets
plt.scatter(W_train, z_train,color='g')
plt.plot(W_train, model.predict(W_train),color='k')
plt.show()

# Plot for Test sets
plt.scatter(W_test, z_test,color='r')
plt.plot(W_test, model.predict(W_test),color='b')
plt.show()
```


![png](Linear_files/Linear_77_0.png)



![png](Linear_files/Linear_77_1.png)


The graph looks better than before and the lines seem to be fitting better. <br>
Now it is time to implement our own model but we will still use the sklearn's score functions. <br>
First way we'll do is using normal equation (closed-form solution) and we will use data that we set the new range.<br>
The formula is as follows.<br>
$$\hat{y} = (X^TX)^{-1}X^Ty$$


```python
X = np.matrix(W_train)
closed_weight = np.linalg.inv(X.transpose() * X) * X.transpose() * np.array(z_train)
```


```python
plt.scatter(W_train, z_train)
plt.plot(W_train, np.multiply(W_train, closed_weight),color='b')
plt.show()
```


![png](Linear_files/Linear_80_0.png)



```python
print('R2 Score : {}'.format(r2_score(z_test, np.multiply(W_test, closed_weight)), 3))
print('MSE : {}'.format(mean_squared_error(np.multiply(W_test, closed_weight), z_test)))
```

    R2 Score : -0.5008834099982729
    MSE : 4987913387.544484
    

The MSE value is bigger than using the linear model and R2 score is worse. It is because there is no bias term in the formula that could shift the function to a certain degree that it originates from (0,0) right now, unlike in the graph of linear regression model. <br>
So, let's use the open-form solution with bias and weight.


```python
def grad_mse_weight(weight, x, y, bias):
    return -2 * np.mean(x * (y - (x * weight + bias)))

def grad_mse_bias(weight, x, y, bias):
    return -2 * np.mean(y - (x * weight + bias))

def linear_model(x, y):
    
    m = 0
    b = 0
    
    learning_rate = 0.001
    iter_num = 20000
    
    for i in range(iter_num):
        
        grad_bias = grad_mse_bias(b, x, y, b)
        grad_weight = grad_mse_weight(m, x, y, b)
        
        b -= grad_bias * learning_rate
        m -= grad_weight * learning_rate
        
    return m, b
        
```

The code above is doing gradient descent on linear regression which we will talk about more in later posts. <br>
To briefly explain what it does, grad_mse functions calculates how much values to add or subtract from weight and bias terms we have. We are iterating 20000 times and in each step, we update our terms little by little (how much is based on the learning_rate we set).

Before going any further, we have to normalize (or re-scale) the data we have because because we are using our own functions. When we use linear models from libraries, it will be taken care of. <br>
The resason for normalization on data is that when we calculate how much to add to the weight or bias, the term could be so big that when we compute <code>np.mean(x * (y - x * weight + bias))</code>.<br>
When the value gets big, it is possible that every iteration it accumulates and reaches to infinity quickly before we could get desired weights and bias. <br>
We are re-scaling so that we could play around with data having managable range of values.


```python
# keep the max values to scale the prediction back to its original form
W_max = np.max(W_test)

# re-scaling
W_train /= np.max(W_train)
z_train /= np.max(z_train)
W_test /= np.max(W_test)
```


```python
m, b = linear_model(W_train['LotArea'], z_train['SalePrice'])
```


```python
plt.scatter(W_train, z_train)
plt.plot(W_train, np.multiply(W_train, m) + b,color='b')
plt.show()
```


![png](Linear_files/Linear_88_0.png)



```python
r2_score(z_train, np.multiply(W_train, m)+b)
```




    0.13115609566876163



It works better than using the closed-form solution!<br>
Now on to predict the prices of test sets.


```python
def linear_predict(X, m, b):
    # scale back the price
    return (X * m + b) * np.max(z_test)[0]
```


```python
pred = linear_predict(W_test['LotArea'], m, b)
```


```python
plt.scatter(W_test, z_test)
plt.plot(W_test, pred,color='r')
plt.show()
```


![png](Linear_files/Linear_93_0.png)



```python
np.round(r2_score(z_test, pred), 3)
```




    0.092



As you can see the score, it has lower value than the training set but it is normal. And it is quite the same to the score of the test sets using the sklearn library! <br>
In the graph above, the x-axis ranges from 0 to 1 but we can always change it back to its original range by simply multiplying the maximum value of the 'LotArea' features.


```python
plt.scatter(W_test*W_max, z_test)
plt.plot(W_test*W_max, pred,color='r')
plt.show()
```


![png](Linear_files/Linear_96_0.png)

