# PrintScript  
Universidad Austral, Ingeniería de Sistemas 2021  
Braian Biale - Agustín de la Vega   
### Branching Strategy: GitFlow

En esta estrategia se utilizan dos branches principales para registrar la historia del proyecto, que son la de master (o main) y la de development, donde la branch de master servirá como la historia oficial de las releases que se saquen y la branch de development servirá como una branch de integracion para las features que se desarrollen. Por otro lado, tambien se van a señalar con un tag a todos los commits de las releases que se integren a master.

Cada nueva feature va a pertenecer a su propia branch, las cuales van a salir de la branch de development. Una vez que se completa la feature, se vuelve a mergear a development. Evitando siempre que estas branches interactuen con master.

Una vez que la branch de development haya acumulado suficientes features para realizar una release, se sale con una branch de release desde development. En este punto ya no se agregan nuevas features a la branch de release actual, solamente se solucionan bugs, y otras tareas relacionadas con la release. Terminadas las tareas antes mencionadas, con la release lista, se mergea a master y se coloca el tag correspondiente a la version nueva.

A su vez tambien se crean hotfix branches de manera de poner realizar arreglos rapidos a las releases que estan en produccion. Son parecidas a las release branches y feature branches salvo que estan basadas en la branch de master en vez que development. Esta es la unica branch que puede salir de master. A penas se completan los arreglos, se debe mergear tanto a master como a development, y se debe agregar un tag a master con la version actualizada.
