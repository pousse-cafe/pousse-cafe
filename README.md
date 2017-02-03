![Travis build status](https://travis-ci.org/pousse-cafe/pousse-cafe.svg?branch=master)
![Maven status](https://maven-badges.herokuapp.com/maven-central/org.pousse-cafe-framework/pousse-cafe/badge.svg)

# Introduction

Pousse-Café is a framework that eases the writing of applications following Domain-Driven Design (DDD) methodology.
It relies on meta-applications described by Domain logic only. Those meta-applications can be integrated in real
applications when augmented with adapters connecting them to the "real" world.

This project is the parent project of all Pousse-Café modules. Pousse-Café is essentially composed of the following
modules:

- Core: Pousse-Café's core components
- Test: Tools helping in writing powerful and Domain-centric unit tests
- Simple App: A template project used to generate related Maven archetype
- Sample Meta App: A sample meta-application implementation show-casing Pousse-Café's features
- Sample App: A sample Spring Boot app show-casing the integration of the Sample Meta App in an actual application
