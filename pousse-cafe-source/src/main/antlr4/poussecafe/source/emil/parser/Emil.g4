grammar Emil;

process : header consumptions ;

header : 'process' (NAME | '*') ;

NAME : [a-zA-Z] [a-zA-Z0-9]* ;

consumptions : consumption* ;

consumption :
    commandConsumption
    | eventConsumption
    ;

commandConsumption : command '->' messageConsumptions ;

command : NAME '?' ;

messageConsumptions :
    singleMessageConsumption
    | multipleMessageConsumptions
    ;

singleMessageConsumption :
    factoryConsumption
    | aggregateRootConsumption
    | repositoryConsumption
    | processConsumption
    | emptyConsumption
    ;

factoryConsumption :
    factoryListener
        ( aggregateRoot '[' 'onAdd' ']' ':' eventProductions )?
    ;

factoryListener : 'F' '{' factoryName=NAME '}' '[' listenerName=NAME ']' ( '#' | '+' )? ;

aggregateRoot : '@' NAME ;

eventProductions : eventProduction+ ;

eventProduction :
    ':' event '#'? '->' messageConsumptions
    ;

event : NAME '!' ;

aggregateRootConsumption :
    'Ru' '{' runnerName=NAME '}'
        aggregateRoot '[' listenerName=NAME ']' ( ':' eventProductions )?
    ;

repositoryConsumption :
    'Re' '{' repositoryName=NAME '}' '[' listenerName=NAME ']'
        ( aggregateRoot '[' 'onDelete' ']' ':' eventProductions )?
    ;

processConsumption : 'P' '{' processName=NAME '}';

emptyConsumption : '.' external? ;

external : '[' NAME ']' ;

multipleMessageConsumptions : multipleMessageConsumptionsItem+ ;

multipleMessageConsumptionsItem : '->' singleMessageConsumption ;

eventConsumption : external? event '->' messageConsumptions ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
