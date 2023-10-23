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

atribuicao &rarr; id opAtribuicao expressao </br>
expressao &rarr; expressao '+' termo | expressao '-' termo | termo </br>
termo &rarr; termo op fator | fator </br>
fator &rarr; id | int | lgc | ltr | txt | dec | lst | '(' expressao ')' </br>

Declaração: 
declaracao &rarr; tipo id 
declaracaoAtribuicao &rarr; declaracao opAtribuicao expressao
expressao &rarr; expressao '+' termo | expressao '-' termo | termo 
termo &rarr; termo op fator | fator 
fator &rarr; id | valor | '(' expressao ')' 

IF:
if &rarr; '?' '<' condicao '>' '{' expressao '}' </br>
ifelse &rarr; if '&' '{' instrucao\* '}' </br>
condicao &rarr; id opL id | id opL expressao | expressao opL expressao | expressao opL id </br>

WHILE:
while &rarr; 'loop' '<' condicao '>' '{' instrucao\* '}'

FOR:
for &rarr; 'looplim' '<' (declaracao | atribuicao) '|' condicao '|' atribuicao '>' {' instrucao\* '}'

Entrada:
ent &rarr; 'ent' '<' tipo ',' id '>'

Print:
print &rarr; 'prt' '<' expressao '>'

Função:
func &rarr; 'fnc' '<' (id)?(, id)* '>' '{' instrucao '}'
