![Travis build status](https://travis-ci.org/pousse-cafe/pousse-cafe.svg?branch=master)
![Maven status](https://maven-badges.herokuapp.com/maven-central/org.pousse-cafe-framework/pousse-cafe/badge.svg)

# Pousse-Café

Pousse-Café is a framework that eases the writing of applications following Domain-Driven Design (DDD) methodology.

It works as follows:
- A Pousse-Café model (i.e. a set of Aggregates and Services) is executed by a Runtime
- Commands are submitted to the Runtime
- Commands are handled by Aggregates using Message Listeners
- Aggregates emit Domain Events
- The set of Message Listeners executed following the submission of a Command defines a Domain Process
- Aggregates may be grouped in Modules
- Domain Events may cross Modules borders

![how it works](https://www.pousse-cafe-framework.org/img/big_picture.svg)

Go to the [web site](https://www.pousse-cafe-framework.org/) for further information.