# Homework-OOP

- VideosDB Nov 2021
- Student: Chiper Alexandra-Diana
- Group: 322CD

## General Considerations

All the classes created for this project (homework) can be found in the package 'actions', were
they are further divided in subpackages by their functionality:
* actions
    * calculators -> thoses classes have specific methods that calculate different things needed
                     by other classes (film rates, serial rates, view counts, etc)  
    * commands -> classes related to the user commands
    * indexFinders -> classes that give back the index o a certain object from their 
                      correspondings arrays in the input
    * queries -> classes that find and build the answer for queries
    * recommendaton -> classes that find and build the answer for recommendations
    * Sorter.java -> a common class that has only one purpose - to sort

## General Structure

Each action has a 'main' class responsible for actually implementing the action (UserCommands, Query, Recommendation), the others are helpers.

* Storage
    * Grader class: it is responsible for keep track of the user commands and storing the rates given to videos

* Answer construction
    * the builder classes: they are given an ordedr list (more specifically a linked hashmap) of potential answers; they apply a filter to each element of the list; if it matches they put the element into the output;

* General Flow (Queries and Recommendations)
    1. from the Main for every action_type the specific object 'execute' method is called.
    2. call the method for the specific type
    3. prepare a linked hashmap to store potential results
    4. call a calculator to populate the map or call another object method that passes an already populated map
        * The calculator may call an IndexFinder if it needs it
    5. call a sorter to sort the now populated map
    6. call a builder and give it the order map and let it do its job
    7. return the string that the builder has put toghether

* General Flow (User commands)
    1. from the Main for every action_type the specific object 'execute' method is called.
    2. call the method for the specific type and use indexFindersi to pass arguments
    3. update the specific Storege structure

## Links
 - For the homework [description](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema)

