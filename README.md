# AgileExpress
'AgileExpress' is a project management system for Agile Projects. The project is like Jira but a simple and user-friendly version for small projects.

## Project Pages
#### Home Page
![image](https://user-images.githubusercontent.com/42180909/187693641-71c17237-1d22-4b0c-b43f-a55aab76b5ae.png)

#### Project Management
![image](https://user-images.githubusercontent.com/42180909/187688917-9574ec4a-faa7-4449-b7fb-de6c691dd06a.png)

#### Profile Page 
![image](https://user-images.githubusercontent.com/42180909/187689091-077fbffd-a9af-4dfb-8667-cdf7ba00880c.png)

#### Search Bar
![image](https://user-images.githubusercontent.com/42180909/187689234-e3945c2f-4422-4fab-9d16-eb164edf8328.png)

#### Task Details
![image](https://user-images.githubusercontent.com/42180909/187689436-6bd9bb5c-3cca-4a94-a5a8-f9d01ebce12c.png)

#### Create Project 
![image](https://user-images.githubusercontent.com/42180909/187689555-0df352ec-650d-42c1-9ea8-25607bc6536d.png)



# Installation

##### Please make sure you have these gadgets installed before using:
* [Docker](https://docs.docker.com/engine/install/)
* [Maven](https://maven.apache.org/index.html) 
* [Git](https://git-scm.com/downloads)

#### Download
> Clone This Project (Make Sure You Have Git Installed)
```
git clone https://github.com/Rabitttt/MovieCollection.git
```

#### Dependencies
> Install Backend Dependencies 
```
cd AgileExpress/
maven install
```
> Install Frontend Dependencies
```
cd AgileExpress/ && cd web
npm install
```
>> If you have a problem with Node versions, force install may be the solution but that may occur some unexpected problems so I don't recommend that.
```
npm install --force
```

#### Run Database

> Up Databases via Docker
```
cd AgileExpress/
sudo docker-compose -f docker-compose.yaml up -d
```

#### Run Project
> Backend 
```
mvn spring-boot:run
(Or just use IDE run button)
```
> Frontend 
```
cd AgileExpress/ && cd web
npm run serve
```


### Login
> Remember this is only an educational purpose application.
The application doesn't have Sign up functionality because of the role management system. There are only 8 users available to test the project.

Roles: UserRoles in the below image are valid.
Username: The user names below are usable.
Password (default): benspassword 

![image](https://user-images.githubusercontent.com/42180909/187694444-1b4530cd-913a-4f94-badc-945abd9ab938.png)
