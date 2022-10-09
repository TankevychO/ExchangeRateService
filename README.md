# Exchange Rate Service 💹

An application is implemented for providing up-to-date currency exchange rates for input currencies. The application consumes exchange rates data from the different 3d party systems via http calls. The application consumes data from the 3d party system, and returns currency exchange rates.
_________________________________________________________________________________________
##### Were used the following sourced:
* [https://freecurrencyapi.net](https://freecurrencyapi.net/)
* [https://exchangeratesapi.io](https://exchangeratesapi.io/)
* [https://www.exchangerate-api.com](https://www.exchangerate-api.com/)
__________________________________________
##### Were implemented such endpoints:
* get exchange rates for currencies
    >[GET: /test-task-over-onix/exchange-rate](http:/localhost:8085/test-task-over-onix/exchange-rate)
* get available currency codes
    >[GET: /test-task-over-onix/currency](http:/localhost:8085/test-task-over-onix/currency) 
* choose the best exchange rate (minimum price for buying or maximum for selling)
    >[GET: /test-task-over-onix/best-rate/{currency}](http:/localhost:8085/test-task-over-onix/best-rate/ALL)
* get exchange rates history by criteria parameters: currency, 3d party system identifier, data range etc.
    >[GET: /test-task-over-onix/history](http:/localhost:8085/test-task-over-onix/history?date=05.10.2022)
#### Params:
 *date* - format (dd.MM.yyyy) </br>
 *idSource* - defaultValue 1 </br>
 *currency* - not required </br>
_________________________________________________________________________________________
#### Run Docker
1) *docker build -t testtaskoveronix-0.0.1-snapshot.jar .* </br>
2) *docker run -d -p 8085:8085 testtaskoveronix-0.0.1-snapshot.jar*  </br>
