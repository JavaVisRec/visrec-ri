# VisRec API JSR381 Reference implementation 
![build](https://github.com/JavaVisRec/visrec-ri/workflows/build/badge.svg) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/javax.visrec/visrec-ri/badge.svg)](https://maven-badges.herokuapp.com/maven-central/javax.visrec/visrec-ri)


The repository contains the source code of the reference implementation of JSR 381, 
a standardization in Java for Visual Recognition.

The Visual Recognition API JSR #381 is a software development standard recognized by the Java Community Process (JCP) that simplifies and standardizes a set of APIs familiar to Java developers for classifying and recognizing objects in images using machine learning. Beside classes specific for visual recognition tasks, it provides general abstractions for machine learning tasks like classification, regression and data set, and reusable design which can be applied for machine learning systems in other domains. At the current stage it provides basic hello world examples for supported machine learning tasks (classification and regression) and image classification.

Reference implementation is based on community edition of Deep Netts Deep Learning Engine available at

 https://github.com/deepnetts/deepnetts-communityedition

Specification for VisRec API is available at

 https://github.com/JavaVisRec/visrec-api

## Getting Started Guide
For step by step guide and additional info see getting started guide at

https://github.com/JavaVisRec/visrec-api/wiki/Getting-Started-Guide

## Quick Start with Examples

Introductory examples are available at

https://github.com/JavaVisRec/jsr381-examples

Quick start with commands:

    git clone https://github.com/JavaVisRec/jsr381-examples.git
    cd jsr381-examples
    mvn clean install
    mvn exec:java -Dexec.mainClass=jsr381.example.ImplementationExample

