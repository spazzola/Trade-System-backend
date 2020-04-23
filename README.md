# Trade-System - version 1.0
Trade system for managing wood trade, making for my family's industry.

Back-end: Java + Spring
Front-end: Vue.js

Application dedicated to sales agents. With that program you can easily manage tradings:
 - you can add:
    - buyers and suppliers
    - product and set price for particual buyer / suppliers
    - invoices for every buyer / supplier
    - orders - sum for each order will be calculated automatically based on price setted for particular product and              buyer / supplier (of course, if you want you can add one-time price for order if you want to)
    
While adding order, you have to select which buyer want to buy product, and from which supplier he will take transport, next you have to select product. Program will calculate particular prices for buyer and supplier, and after that it will subtract particular values from relevant invoices. If buyer / suppplier haven't paid for invoice, then will be generated "negative invoice" which will decrease amount of next added invoice. 
   
User has possibility to check current balance of every buyer / supplier based on paid invoices.
User can generate monthly / yearly report as well.

Next version will be merged with company's web-page. Front-end will be re-write to make it more clean.
    
