### easyInject-Library
___
Микро библиотека для инджекта в рантайме.  
Можно инджектить поля при помощи аннотации @Inject.  
Параметр key аннотации @Inject указывает какой ключ будет искаться в массиве строк.  
Инджектятся даже приватные поля.  
___
Library for inject in runtime.  
You can inject fields with annotation @Inject.  
The key parameter of the @Inject annotation specifies which key will be searched in the array of strings.  
Even private fields are injected.  
___
Пример:  
Example:  

> public class Test {  
>> @Inject(key = "integer")  
>> private int testField;  
>>
>> public static void main(String[] args) {  
>>> Test temp = new Test();  
>>> AnnotationProcessor.getProcessor.inject(temp, args);
>>
>> }  
>
> }<br/>

В данном примере будем считать что в аругменты программы приходит строка "integer=4235".  
AnnotationProcessor выполнит инджект в приватное поле testField объекта temp  
и в поле будет храниться значение 4235.  
Работает со всеми примитивными типами и классом String.  
