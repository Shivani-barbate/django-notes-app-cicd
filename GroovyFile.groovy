pipeline{
    agent any
    
    stages{
        stage("Cleanup"){
            steps{
                cleanWs()
            }
        }
        stage("Code"){
            steps{
                echo "Cloning the code"
                git url: "https://github.com/Shivani-barbate/django-notes-app-cicd", branch: "main"
            }
        }
        stage("Build"){
            steps{
                echo "BUilding the code"
                 sh "docker build -t notes-app ."
            }
        }
        stage("Push to DH"){
            steps{
                echo "Pushing to dockerhub"
                withCredentials([usernamePassword(credentialsId:"DockerHub",passwordVariable:"DockerHubPass",usernameVariable:"DockerHubUser")]){
                sh " docker tag notes-app ${env.DockerHubUser}/notes-app:latest"
                sh " docker login -u ${env.DockerHubUser} -p ${env.DockerHubPass}"
                sh " docker push ${env.DockerHubUser}/notes-app:latest "
                }
                
            }
        }
        stage("Deploy"){
            steps{
                echo "Deploying to container"
                sh "docker-compose down && docker-compose up -d"
               
            }
        }
        
    }
}
