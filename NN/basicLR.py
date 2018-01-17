#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2017/10/13 21:52
# @Author  : Aries
# @Site    : 
# @File    : basicLR.py
# @Software: PyCharm
import numpy as np
import matplotlib.pyplot as plt
from numpy import *


def sigmoid(x):
    return 1/(1+exp(-x))


def costFunction(theta,X,y):
    m = X.shape[0]
    h = sigmoid(X.dot(theta))
    j = (-1.0/m)*(np.log(h).T.dot(y)+np.log(1-h).T.dot(1-y))
    return j

def costFunction1(theta,X,y):
    m = X.shape[0]
    h = sigmoid(X.dot(theta.transpose()))
    j = (-1.0/m)*(np.log(h).T.dot(y)+np.log(1-h).T.dot(1-y))
    return j

def gradAscent(dataMatIn,classLabels):
    dataMatrix = mat(dataMatIn)
    labelMat = mat(classLabels).transpose()
    m,n = shape(dataMatrix)
    alpha = 0.001
    maxCycles = 2000
    weights = ones((n,1))
    iters = []
    cost = []
    for k in range(maxCycles):
        h = sigmoid(dataMatrix*weights)
        error = (h - labelMat)
        iters.append(k)
        cost.append(costFunction(weights,dataMatrix,labelMat))
        weights -= alpha*(1.0/m)*dataMatrix.transpose()*error

    fig = plt.figure()
    ax = fig.add_subplot(111)
    ax.scatter(iters,cost)
    plt.show()
    return weights

def stocGradAscent0(dataMatIn,classLabels):
    dataMatrix = mat(dataMatIn)
    labelMat = mat(classLabels).transpose()
    m, n = shape(dataMatrix)
    alpha = 0.01
    weights = ones((1, 3))
    iters = []
    cost = []
    for i in range(m):
        h = sigmoid(weights*dataMatrix[i].transpose())
        error = (h - classLabels[i])
        print(error)
        iters.append(i)
        cost.append(costFunction1(weights, dataMatrix, labelMat))
        weights -= alpha * (1.0 / m) * error * dataMatrix[i]

    fig = plt.figure()
    ax = fig.add_subplot(111)
    ax.scatter(iters, cost)
    plt.show()
    return weights

def newton(dataMatIn,classLabels):
    dataMatrix = mat(dataMatIn)
    labelMat = mat(classLabels).transpose()
    m, n = shape(dataMatrix)
    alpha = 0.001
    maxCycles = 500
    weights = ones((n, 1))
    iters = []
    cost = []
    for k in range(maxCycles):
        h = sigmoid(dataMatrix * weights)
        error = (h - labelMat)
        z = np.multiply(h,1-h)
        iters.append(k)
        cost.append(costFunction(weights,dataMatrix,labelMat))
        H = np.mat(np.ones((n,n)))
        for i in range(n):
            for j in range(n):
                H[i,j] = (1.0/m)*np.sum(np.multiply(z,np.multiply(dataMatrix[:,i],dataMatrix[:,j])))
        J = (1.0/m)*dataMatrix.transpose()*error
        weights -= alpha*H.I.dot(J)

    fig = plt.figure()
    ax = fig.add_subplot(111)
    ax.scatter(iters,cost)
    plt.show()
    return weights


data = [[1,1,3],[1,2,3],[1,2,1],[1,3,2]]
label = [1,1,0,0]
gradAscent(data,label)
stocGradAscent0(data,label)
newton(data,label)
