/*

  Project Title: Smart Touch Key
  Authors: Shubham Lingayat & Swanand Buva
  File: extension\content.js
  For more info .. 
  GitHub LINK: https://github.com/suebham/Smart-Touch-Key
  Don`t Forget to give a Star on GitHub

*/

//For flag 'inputhai'
var inputhai=0;
var x = document.querySelectorAll("input[type=text], input[type=email]");
var pass = document.querySelectorAll("input[type=password]");
//var forms = document.querySelectorAll('button[type="submit"]');
//Array x(username field) filter
    var user = [];
    for(var j=0; j<x.length; j++)
    {
      var ye = x[j].name;
      var matchflag=0;
      var prefferedPatterns = [".*user", ".*User", ".*login", ".*email", ".*identifier", ".*session", ".*uname", ".*username"];
      for (i=0; i<prefferedPatterns.length;i++) {
       if(ye.match(RegExp(prefferedPatterns[i]))) {
        matchflag = 1;
        inputhai = 1;
       }
      }
      if(matchflag == 1) {
         user.push(x[j]);
      }
    }
  console.log("STK : The Fieds found are:-");
  for(var i=0; i<user.length; i++) {
      console.log(user[i]);
    }
  console.log(" ");


//Evaluating inputhai
var loc = window.location.hostname;
if(inputhai == 1) {

  //For checking the connection status between 1: Extension & Java Program 2: Java Program & Android App
  //var connstatus=0;
  var request=new XMLHttpRequest();
    request.onreadystatechange=function(){
        if (request.readyState == 4) {
            if (request.status == 200){
                var rj=JSON.parse(request.responseText)
                console.log(rj);
                if(rj.res == "true")
                {
                  //connstatus=1;
                  console.log("Connection OK");
                  console.log("Calling Bhejo()");
                  bhejo();
                }
                else
                {
                  //connstatus=0;
                  console.log("Connection is Broke between Java Program and Android");
                }
              }
            }
            else{
              console.log("Error: "+request.status+request.statusText);
              console.log("Connection is Broke between Java Program and Extension");
              //connstatus=0;
            }
        }
    request.open("POST","http://127.0.0.1:8000/GET",true);
    var message="{'msg':1}";
    console.log("Sending : "+JSON.stringify(message));
    request.send(message);
    console.log("Checking Connection");

  function bhejo() {
  var loc = window.location.hostname;
  console.log("STK : Window.location = " + loc + " sending to Server (Java Program, which forwards it to the Android App.");
  var request=new XMLHttpRequest();
    request.onreadystatechange=function(){
        if (request.readyState == 4) {
            if (request.status == 200){
              var rj=JSON.parse(request.responseText)
              console.log(rj);
              if(rj.isFound == "true"){
                var username=rj.user;
                var password=rj.pass;
                bhardo(username, password);
              }
            }
            else
                console.log("Error: "+request.status+request.statusText);
        }
    }
    request.open("POST","http://127.0.0.1:8000/GET",true);
    var web = "'"+loc+"'";
    var message="{'web':"+web+", 'msg': '2'}";
    console.log("Sending : "+JSON.stringify(message));
    request.send(message);
    console.log("Sent");
    console.log("");
  }
}
else {
  console.log("STK ran successfully, but no input field found!");
}

//Function for filling the fetched data
function bhardo(username, password){
  console.log("Fuction called for filling the fields");
  for(var i = 0; i < user.length; i++) { user[i].value = username; }
  for(var i = 0; i < pass.length; i++) { pass[i].value = password; }
  //for(var i = 0; i < pass.length; i++) { forms[i].click(); }
}