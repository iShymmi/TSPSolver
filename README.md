# TSPSolver

## Table of Contents  
1. [Overview](#overview)  
2. [Theoretical basics](#thbasic)  
  2.1 [Travelling Salesman Problem](#tsp)  
  2.2 [Genetic Algorithm](#ga)
3. [Algorithm](#algorithm)
4. [Performed operations](#operations)  
  4.1 [Mutatioin](#mutation)  
  4.2 [Breed](#breed)  
  4.3 [Selection](#selection)
5. [Usage](#usage)
6. [Testing application](#application)  
  6.1 [Installation](#installation)  
  6.2 [Preview](#preview) 

<a name="overview" />  

## 1. Overview  

The project is to implement my own interpretation of the genetic algorithm to solve the
**Travelling Salesman Problem**

<a name="thbasic"/>  

## 2. Theoretical basics

<a name="tsp" />  

### 2.1 Travelling Salesman Problem

TSP is the challenge of finding the shortest yet most efficient route for a person to take given a list of specific destinations. 
It is a well-known algorithmic problem in the fields of computer science and operations research.

<a name="ga"/>  

### 2.2 Genetic Algorithm  

[Source](https://en.wikipedia.org/wiki/Genetic_algorithm)  

GA is a metaheuristic inspired by the process of natural selection that belongs to the larger class of evolutionary algorithms (EA). 
Genetic algorithms are commonly used to generate high-quality solutions to optimization and search problems 
by relying on biologically inspired operators such as mutation, crossover and selection.

More in  `David E. GoldbergGenetic Algorithms in Search, Optimization and Machine Learning book`

<a name="algorithm"/>  

## 3. Algorithm

![FlowchartDiagram](https://user-images.githubusercontent.com/15768915/114885902-63210480-9e07-11eb-974f-665f3abfca12.png)

<a name="operations"/>

## 4. Performed operations  

**First and last gene can't be changed**

<a name="mutation"/>

### 4.1 Mutation

    1) Select two random genes from Individual
    2) Swap them places
    3) Return individual with a new set of genes

   <table>
   <thead>
   <tr>
     <td>State</td>
     <td>Individual</td>
     <td>Random genes</td>
   </tr>
   </thead>
   <tbody>
   <tr>
     <td>Before</td>
     <td>B – C – <strong><em>D</em></strong> – I – H – <strong><em>F</em></strong> – E – G – B</td>
     <td>D , F</td> 
   </tr>
   <tr>
     <td>After</td>
     <td>B – C – <strong><em>F</em></strong> – I – H – <strong><em>D</em></strong> – E – G – B</td>
   </tr>
   </tbody>
   </table>
   
<a name="breed"/>
   
### 4.2 Breed

    1) Set random crossing point
    2) Create new individual as follow :
       - Genes from first individual in order till the crossing point
       - All missing genes from first individual in order of second individual 
    3) Return new individual with a new set of genes

   <table>
   <thead>
   <tr>
     <td>Mark</td>
     <td>Individual</td>
     <td>Genes from cross point</td>
     <td>Ordered Genes </td>
   </tr>
   </thead>
   <tbody>
   <tr>
     <td>First</td>
     <td>B – C – D – I – <strong><em>G – F – E - H - B</strong></em></td>
     <td>G – F – E - H</td>
     <td></td>
   </tr>
   <tr>
     <td>Second</td>
     <td>I – <strong><em>H</strong></em> – <strong><em>F</strong></em> – C – D – B – <strong><em>E</strong></em> - <strong><em>G</strong></em> - B</td>
     <td></td>
     <td>H – F – E - G</td>
   </tr>
   <tr>
   <td>New</td>
   <td><strong><em>B – C – D – I</strong></em> – H –F  – E – G - B</td>
   </tr>
   </tbody>
   </table>
   
<a name="selection"/>
   
### 4.3 Selection + Elite model
  For this algorithm I used a **fitness proportionate selection**, known as well as **roulette wheel selection**. Best individual would be the one with the
  shortes route length. As originally roulette wheel selection is used to calculate the optimization problem which solution is to find the largest value of a certain problem parameter. To use this selection method we need to transform the TSP problem into an optimization problem in which the highest value is the most significant.
  
  **Steps to reach that**
      
     1) Reverse the length for each individual by multiplying it by -1
     2) Pick the length of the worst individual
     3) Add the original length of the worst individual to reversed length of each individual
     4) Sum the reversed lengths

  **Selection**
   
     1) Save the best individual in the pocket
     2) Determine the sector size for each individual
        (reversed length of individual / sum of reversed lengths)      
     3) Draw random number from 0 to 1, and pick individual which sector fits this number
     4) Add selected individual to new population group with the size of original population size  
     5) Repeat points 3 and 4 until new population would be full
     6) If the best individual has not been selected, replace the last individual in the group with the best individual
     7) Swap the current population with new population

<a name="usage"/>

## 5. How to use
   1) Create new instance of TSPSolver  
   `TSPSolver myTSPSolver = new TSPSolver(distances, startIndex, populationSize, stopCondition,
                     crossingPickProbability, mutationPickProbability);`  
   or  
   `TSPSolver myTSPSolver = new TSPSolver();` and set required fields
   
   <table>
   <thead>
   <tr>
     <td>Variable</td>
     <td>Type</td>
     <td>Example</td>
   </tr>
   </thead>
   <tbody>
   <tr>
     <td>distances</td>
     <td>double[][]</td>
     <td>  
       
      0  16 47 72 77
      16 0  37 57 65  
      47 37 0  40 30  
      72 57 40 0  31  
      72 65 30 31 0  
      
   </td> 
   </tr>
   <tr>
     <td>startIndex</td>
     <td>int</td>
     <td>5</td>
   </tr>
   <tr>
     <td>populationSize</td>
     <td>int</td>
     <td>5000</td>
   </tr>
   <tr>
     <td>stopCondition</td>
     <td>int</td>
     <td>15 000</td>
   </tr>
   <tr>
     <td>crossingProbability</td>
     <td>double</td>
     <td>0.5</td>
   </tr>
   <tr>
     <td>mutationProbability</td>
     <td>double</td>
     <td>0.7</td>
   </tr>
   </tbody>
   </table>
   
   2) Use myTSPSolver.run() to run 
   3) Pick best individual after algorithm stops

<a name="application"/>

## 6. Testing application

<a name="installation"/>

### 6.1 Installation

:warning: Java 11 Required to run this algorithm

**Compile**
```
mvn clean install
```
**Run**
```
mvn exec:java -Dexec.mainClass=testingApp.AppMain
```

<a name="preview"/>

### 6.2 Preview

![1](https://user-images.githubusercontent.com/15768915/115063038-55dd4600-9eeb-11eb-9199-30f9362ef3f0.png)
![2](https://user-images.githubusercontent.com/15768915/115063084-642b6200-9eeb-11eb-8518-976de73ec651.png)
![3](https://user-images.githubusercontent.com/15768915/115063085-642b6200-9eeb-11eb-8059-5f96b00aabc5.png)
