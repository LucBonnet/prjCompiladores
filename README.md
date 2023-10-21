Gramáticas:

tipo &rarr; 'dec' | 'int' | 'lgc' | 'ltr' | 'txt' </br>
id &rarr; [(a-zA-Z)+(a-zA-Z0-9)\*] </br>
int &rarr; [0-9]+  </br>
dec &rarr; [(0-9)+(.0-9)\*]  </br>
lgc &rarr; 'fls' | 'ver' </br>
ltr &rarr; [a-zA-Z] </br>
txt &rarr; '"' [(a-zA-Z)+(a-zA-Z0-9)\*] '"' </br>
opAtribuicao &rarr; '->'  </br>
opM &rarr; '*' | '/' | 'rst' | '^' </br>
opL &rarr; 'mnr' | 'mar' | 'equ' | 'mnri' | 'mari' </br>
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
