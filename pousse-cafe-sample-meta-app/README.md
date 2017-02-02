# Domain

This sample meta-application describes the domain of an e-shop system where a given number of units of a *Product* are
available in-stock and ready to be bought by *Customers*. When a Customer wants to buy a given number of units of a
Product, it first needs to place an *Order*. Once that Order has been settled by the Customer, the ordered Product units
are sent to the Customer. 

It is expected that several *Messages* are sent to the Customer at several key steps of above process:

- When the Order cannot be accepted because of an insufficient number of available units
- When the Order is ready to be settled
- When the Order is successfully settled (a confirmation message)
- When the Order is ready to be sent

Obviously, before being able to order Product units, those need to be available. It should therefore be possible to
create Products and add available units to them.
