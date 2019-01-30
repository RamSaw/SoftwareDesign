[![Build Status](https://travis-ci.org/RamSaw/SoftwareDesign.svg?branch=master)](https://travis-ci.org/RamSaw/SoftwareDesign)
# SoftwareDesign
Repository for software design course assignments

Такой была prescriptive архитектура:
![alt text](https://github.com/RamSaw/SoftwareDesign/blob/hw01/PrescriptiveArchitecture.png)

Она очень похожа на ту, что обсуждали на паре.

Сейчас архитектура такая:
<img src="https://github.com/RamSaw/SoftwareDesign/blob/hw01/Architecture.png" width="2500" height="500">
![alt text](https://github.com/RamSaw/SoftwareDesign/blob/hw01/Architecture.png | width=250)

Смысл ее следующий: все начинается со статического метода run, при этом уже на старте приложения создается singleton GlobalEnvironment, о котором знают все классы и он хранит глобальные переменные. Далее run вызывает process для каждой введеной строки, этот метод обрабатывает строку следующим образом: сначала вызывает InterpolationLexer, которые подставляет переменные и разбивает ввод на токены. Далее токены передаются парсеру ExecutionParser, который возвращает Executable. Парсер может кидать исключения: UnknownCommandException если команда в pipe пустая, WrongCommandArgumentsException если неправильное количество аргументов у присваивания. Как говорилось, парсер создает правильную иерархию Executabl'ов и возвращает некоторую реализацию Executable, у которого можно вызвать метод execute и получить результат работы введеной строки. Результат это либо строка, либо null. На данный момент все команды - это команды которые бывают в pipe, поэтому все наследуются от класса PipelineExecutable. У него есть два наследника: NoArgumentsExecutable и OneTypeArgumentsExecutable<T>. Первый это типа команды без аргументов (аргументы игнорируются если переданы), второе это тип команды, принимащей аргументы одного типа, где T и есть этот тип. Наследники NoArgumentsExecutable: Exit и Pwd, OneTypeArgumentsExecutable<T>: Cat, Echo, Wc и UnknownCommand. UnknownCommand - создается при неизвестной команде во входе и вызывает внешнюю программу на компьютере, может кинуть ExternalCommandException, если входе исполнения внешней программы произошла ошибка. Есть еще комманда "=", которая реализуется классом Assignment и наследуется сразу от PipelineExecutable, потому что требует равно два аргумента типа String. Есть отдельный пакет с исключениями, все они описаны выше.
