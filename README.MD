# Sincronização de Contas do Sicredi na Receita Federal

## Demanda

### Cenário de Negócio:
Todo dia útil por volta das 6 horas da manhã um colaborador da retaguarda do Sicredi recebe e organiza as informações de contas para enviar ao Banco Central. Todas agencias e cooperativas enviam arquivos Excel à Retaguarda. Hoje o Sicredi já possiu mais de 4 milhões de contas ativas.
Esse usuário da retaguarda exporta manualmente os dados em um arquivo CSV para ser enviada para a Receita Federal, antes as 10:00 da manhã na abertura das agências.

### Requisito:
Usar o "serviço da receita" (fake) para processamento automático do arquivo.

### Funcionalidade:
0. Criar uma aplicação SprintBoot standalone. Exemplo: java -jar SincronizacaoReceita <input-file>
1. Processa um arquivo CSV de entrada com o formato abaixo.
2. Envia a atualização para a Receita através do serviço (SIMULADO pela classe ReceitaService).
3. Retorna um arquivo com o resultado do envio da atualização da Receita. Mesmo formato adicionando o resultado em uma nova coluna.


### Formato CSV:
agencia;conta;saldo;status
0101;12225-6;100,00;A
0101;12226-8;3200,50;A
3202;40011-1;-35,12;I
3202;54001-2;0,00;P
3202;00321-2;34500,00;B


## Como executar
Foram criados dois microserviços para a solução dessa demanda.
-sicredisincronizacontas - Responsável por enviar os dados do arquivo_receita.csv para a Receita Federal, utilizando a mensageria com o RabbitMQ.
-atualizadorcontasreceita - Simula o serviço da Receita Federal, onde vão ser sicronizadas e validadas as contas de acordo uma série de verificações, padronizadas pela Receita. Consome os dados imputados 
Abaixo segue o script para configuração e execução dos microserviços.


#### 1º - Baixar os microserviços
-Como eu não fiz o gerenciamento de commits pelo github, subi os dois microserviços na mesma pasta, junto com outros arquivos que serão necessários para a configuração e execução dos microserviços.
-Instalar no STS um de cada vez, primeiro o "sicredisincronizacontas", depois o "atualizadorcontasreceita".
-Executar o RabbitMQ no Docker, pelo arquivo docker-compose.yml
####OBS: 
Não excutar o "maven install" até configurar o RabbitMQ, pois se for executado, ficará dando exceções no log do sistema até eles encontrarem as configurações do RabbitMQ.


#### 2º - Executar o Docker conmpose:
-Para executar o arquivo docker-compose.yml que acompanha a aplicação devem ser executados os comandos:

docker-compose up -d - Para subir o RabbitMQ 

docker-compose ps - para verificar se esta no ar o RabbitMQ

-Acessar no browser o link http://localhost:15672/
username: admin
password: admin


#### 3º - Configuração RabbitMQ
Para configurar a correta execução da aplicação integrando ao RabbitMQ devem ser feitas as seguintes configurações:

-No menu Queue, deve ser adicionado a chave "sincronizador.conta.queue"
-Ao subir a aplicação "sicredisincronizacontas", no menu Exchange vai aparecer a chave "sincronizador.exchange".
-Ao clicar na chave "sincronizador.exchange", deve ser feita a integração, 
clicando em "Bind", configurando as chaves "sincronizador.conta.queue" e "sincronizador.conta.routingkey".
-Se subir outra vez a aplicação "sicredisincronizacontas", ele deverá enviar os dados de forma assíncrona para o RabbitMQ.
-Para verificar o que ocorreu com a execução da aplicação , deverá ser consultado o arquivo de log "sicredisincronizacontas.log".

-Ao executar a aplicação "atualizadorcontasreceita", vão ser consumidos os dados que estiverem assíncronos no RabbitMQ, e retornará mensagem de validações, gravadas no arquivo de log "atualizadorcontasreceita.log".


#### 4º - Executar os microserviços
-O arquivo "arquivo_receita.csv", de onde serão retirados os dados para envio a Receita, esta configurado para ser lido da raiz do projeto, se estiver em outro lugar não vai funcionar.
-Todas as vezes que executar o microserviço "sicredisincronizacontas", ele vai ler o arquivo .csv, e vai enviar os dados que estiverem configurados para o RabbitMQ.
-Todas as vezes que executar o microserviço "atualizadorcontasreceita", seram consumidos os dados que foram enviados ao RabbitMQ, se houver dados.
-Os retornos dos microserviços serão gravados a partir de suas execuçções, e serão gravados em arquivos .log, e com o mesmo nome do microserviços.
-Para verificar se o microserviço executou corretamente, poderá ser consultado posteriormente os logs.
###OBS Importante:
-Como o volume do RabbitMQ no Docker não esta configurado para ficar gravado junto a aplicação, ele vai apagar as configurações de intgração com a applicação, que deverão ser refeitas novamente.



