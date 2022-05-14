/*

  Project Title: Smart Touch Key
  Authors: Shubham Lingayat & Swanand Buva
  File: extension\popup.js
  For more info .. 
  GitHub LINK: https://github.com/suebham/Smart-Touch-Key
  Don`t Forget to give a Star on GitHub
  
*/
var apnaArray = [""];
document.addEventListener('DOMContentLoaded', function() {


    var disCount = 0;
    var ConCount = 0;
    //For GitHub Link
    var links = document.getElementsByTagName("a");
    for (var i = 0; i < links.length; i++) {
        (function () {
            var ln = links[i];
            var location = ln.href;
            ln.onclick = function () {
                chrome.tabs.create({active: true, url: location});
            };
        })();
    }

    //var isConnected=0;
    var request=new XMLHttpRequest();
    request.onreadystatechange=function(){
        if (request.readyState == 4) {
            if (request.status == 200){
                var rj=JSON.parse(request.responseText)
                console.log(rj);
                if(rj.res == "true")
                {
                  //var isConnected=1;
                  console.log("REQ Connected");
                  //MicCheck("Connected");
                  Print("Connected");
                }
                else if(rj.res == "false")
                {
                    //var isConnected=0;
                    console.log("Connection is Broke between Java Program and Android");
                    //MicCheck("Disconnected");
                    Print("Disconnected");
                }
            }
        }
        else{
            console.log("Error: "+request.status+request.statusText);
            console.log("Connection is Broke between Java Program and Extension");
            //var isConnected=0;
            //MicCheck("Disconnected");
            Print("Disconnected");
        }
    }
    request.open("POST","http://127.0.0.1:8000/GET",true);
    var message="{'msg':1}";
    console.log("Sending : "+JSON.stringify(message));
    request.send(message);


    function Print(string){

        if(string == "Connected"){
            ConCount++;
        }
        if(string == "Disconnected"){
            disCount++;
        }
        if(ConCount >= 1) {
            var child = document.createTextNode("Connected");
            printelement.appendChild(child);
            document.body.appendChild(printelement);
            document.getElementById('haha').innerHTML = "Shubham";
        }
        else if(disCount >= 1 && ConCount==0)
        {
            var printelement = document.createElement("lol");
            var child = document.createTextNode("Disconnected");
            printelement.appendChild(child);
            document.body.appendChild(printelement);
        }
        setInterval(function(){ console.log(ConCount);
            console.log(disCount); }, 3000);
    }
});



