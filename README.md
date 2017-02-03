![Travis build status](https://travis-ci.org/pousse-cafe/pousse-cafe.svg?branch=master)
![Maven status](https://maven-badges.herokuapp.com/maven-central/org.pousse-cafe-framework/pousse-cafe/badge.svg)

# Introduction

Pousse-Café is a framework assisting you in writing applications following Domain Driven Design methodology. While DDD
has proven to be useful in writing code representing complex business rules and/or having strong scalability requirements,
it is not always easy to apply given the number of transversal issues that need to be addressed. Pousse-Café's purpose
is to solve most of those issues allowing developers to focus on the Domain and its implementation.

This project is the parent project of all Pousse-Café modules. Pousse-Café is essentially composed of the following
modules:

- Core: Pousse-Café's core components
- Test: Tools helping in writing powerful and Domain-centric unit tests
- Simple App: A template project used to generate related Maven archetype
- Sample App Domain: A sample domain implementation show-casing Pousse-Café's features
- Sample App: A sample Spring Boot app show-casing the integration of Sample App Domain in an actual application
