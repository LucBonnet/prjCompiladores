Gramáticas:

tipo &rarr; 'dec' | 'int' | 'lgc' | 'ltr' | 'txt' 
id &rarr; [(a-zA-Z)+(a-zA-Z0-9)\*]
int &rarr; [0-9]+
dec &rarr; [(0-9)+(.0-9)\*]
lgc &rarr; 'fls' | 'ver'
ltr &rarr; [a-zA-Z]
txt &rarr; '"' [(a-zA-Z)+(a-zA-Z0-9)\*] '"'
opAtribuicao &rarr; '->' 
opM &rarr; '*' | '/' | 'rst' | '^'
opL &rarr; 'mnr' | 'mar' | 'equ' | 'mnri' | 'mari'
valor &rarr; int | dec | lgc | ltr | txt

Atribuição:

atribuicao &rarr; id opAtribuicao expressao
expressao &rarr; expressao '+' termo | expressao '-' termo | termo
termo &rarr; termo op fator | fator
fator &rarr; id | int | lgc | ltr | txt | dec | lst | '(' expressao ')'

Declaração: 
declaracao &rarr; tipo id opAtribuicao expressao | tipo id
expressao &rarr; expressao '+' termo | expressao '-' termo | termo
termo &rarr; termo op fator | fator
fator &rarr; id | valor | '(' expressao ')'

IF:
if &rarr; '?' '<' condicao '>' '{' expressao '}'
ifelse &rarr; if '&' '{' expressao '}'
condicao &rarr; id opL id | id opL expressao | expressao opL expressao | expressao opL id
