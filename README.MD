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

-sicredisincronizacontas - Conforme solicitado para a funcionalidade, deverá ser executado pelo prompt do windows o comando java -jar SincronizacaoReceita <input-file>,
onde lerá o arquivo .csv e enviará o serviço que simula a Sincronização de Contas na Receita Federal, utilizando mensageria com o RabbitMQ.
-atualizadorcontasreceita - Simula o serviço da Receita Federal, onde vão ser sicronizadas e validadas as contas de acordo uma série de verificações, padronizadas pela Receita. Consome os dados imputados 
Abaixo segue o script para configuração e execução dos microserviços.


#### 1º - Baixar os microserviços
-Como eu não fiz o gerenciamento de commits pelo github, subi os dois microserviços na mesma pasta, junto com outros arquivos que serão necessários para a configuração e execução dos microserviços, inclusive o sicredisincronizacontas-0.0.1-SNAPSHOT.jar, que é o microserviço do sicredi componentizado.
-Como o primeiro microserviço, o "sicredisincronizacontas", deve ser executado apenas por linha de comando, o código dele esta indo junto para que possa verificar como foi construido.

-Instalar no STS um de cada vez, primeiro o "sicredisincronizacontas", depois o "atualizadorcontasreceita".
-Deve ser executado o comando "maven install", para baixar as dependencias utilizadas no desenvolvimento de ambos microserviços.
-Executar o RabbitMQ no Docker, pelo arquivo docker-compose.yml
####OBS: 
Não excutar os microsrviços antes de fazer a configuração  do RabbitMQ, pois se forem executados, resultará em exceções no log do sistema até eles encontrarem as configurações do RabbitMQ.


#### 2º - Executar o Docker compose:
-Para executar o arquivo docker-compose.yml que acompanha a aplicação devem ser executados os comandos:

###docker-compose up -d 
-Para subir o RabbitMQ 

###docker-compose ps
-Para verificar se esta no ar acessar no browser o link http://localhost:15672/, e usar as credencias abaixo para login
username: admin
password: admin


#### 3º - Configuração RabbitMQ
Para configurar a correta execução da aplicação integrando ao RabbitMQ devem ser feitas as seguintes configurações:

#### Para o microserviço "sicredisincronizacontas"
-No menu Queue, deve ser adicionado a chave "sincronizador.conta.queue"
-No menu Exchange, em "Add a new exchange", deve ser inserida a chave "atualizador.exchange".
-Clicar na chave "sincronizador.exchange", para fazer a integração, no componente "Add binding from this exchange", no campo "To queue" inserir a chave "sincronizador.conta.queue" e no campo "Routing key" inserir  a chave "sincronizador.conta.routingkey", clicando em "Bind" para finalizar.
-Ao subir a aplicação "atualizadorcontasreceita", no console do Eclipse, é possivel ver as urls de integração, e ele deverá enviar os dados de forma assíncrona para o RabbitMQ.
-O microserviço esta configurado para gerar o arquivo de log "sicredisincronizacontas.log".

#### Para o microserviço "atualizadorcontasreceita"
-No menu Queue, deve ser adicionado a chave "atualizador.conta.queue"
-No menu Exchange, em "Add a new exchange", deve ser inserida a chave "atualizador.exchange".
-Clicar na chave "atualizador.exchange", para fazer a integração, no componente "Add binding from this exchange", no campo "To queue" inserir a chave "atualizador.conta.queue" e no campo "Routing key" inserir  a chave "atualizador.conta.routingkey", clicando em "Bind" para finalizar.
-Ao subir a aplicação "atualizadorcontasreceita", no console do Eclipse, é possivel ver as urls de integração, e ele deverá enviar os dados de forma assíncrona para o RabbitMQ.
-O microserviço esta configurado para gerar o arquivo de log "sicredisincronizacontas.log".


#### 4º - Executar os microserviços
-Ressaltando, o arquivo .csv deve ser executado, conforme solicitado na demanda, pela linha de comando java -jar SincronizacaoReceita <input-file>, onde serão retirados os dados para envio a Receita. Deve enviar o caminho do arquivo junto ao nome.
-Todas as vezes que executar o microserviço "sicredisincronizacontas", ele vai ler o arquivo .csv, e vai enviar os dados que estiverem configurados para o RabbitMQ.
-Todas as vezes que executar o microserviço "atualizadorcontasreceita", seram consumidos os dados que foram enviados pelo microservico "sicredisincronizacontas" ao RabbitMQ, se houver dados.
-A resposta do simulador da Receita Federal, enviará os dados de volta, onde gerará um novo arquivo, chamado "arquivo_receita_resposta.csv", onde constará uma coluna a mais para a verificação da Sincronização.
-Os retornos dos microserviços serão gravados a partir de suas execuções, em arquivos .log, e com o mesmo nome do microserviços. Para verificar se o microserviço executou corretamente, poderá ser consultado posteriormente os logs.
###OBS Importante:
-Como o volume do RabbitMQ no Docker não esta configurado para ficar gravado junto a aplicação, ele vai apagar as configurações de intgração com a applicação, que deverão ser refeitas novamente.



