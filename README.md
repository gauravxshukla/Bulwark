# Bulwark (Device Based Password Management System)
Password manager systems currently available for the users come with serious privacy and vulnerability issues of their own. This paper gives an introduction to Bulwark, a novel device-based password management system, which stores the login credentials on a mobile device, and can enter the credentials on any target device through an encrypted cross-platform system, only after being authenticated by the user biometrics to ensure that only the right user can fill the saved passwords. This technology aims to eliminate the need to trust and rely on third-party cloud-based services or computers with storing and managing our passwords, and has also resolved major privacy and vulnerability issues with the current options available for users.

# Proposed Solution
We designed the application that manages the storage and retrieval of passwords for different website accounts and the application. We used SQLite database for the storage of users’ credentials which are located in personal mobile devices. The passwords can be viewed or filled in only through fingerprint authentication. The architecture of the system is consisting of three main components, i.e. the Android Application, Browser Extension, and Native Java Server Program.

# Architectural Design Of Bulwark
![image](https://user-images.githubusercontent.com/82375003/171283648-12de64d4-fa6f-4abc-ad50-3b5b9263c095.png)


# Live Working of System - 

https://user-images.githubusercontent.com/82375003/171283885-f6d928a1-6016-452e-b854-a306a8454c71.mp4


Research Paper - http://sciencescholar.us/journal/index.php/ijhs/article/view/7531

• Tech Stack - Browser Extension, Java Server Program, Android Application.
