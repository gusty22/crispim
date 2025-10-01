pipeline {
    agent { docker { image 'maven:3.8.5-openjdk-17' } }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/crispimluiz/aula_thymeleaf_java'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Testes Unitários') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/surefire-reports/TEST-*.xml'
                }
            }
        }
        stage('Docker Build Desenvolvimento') {
            steps {
                script {
                    def appName = "aula_thymeleaf_java"
                    def appVersion = sh(script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim()
                    def imageName = "${appName}:${appVersion}-dev"
                    docker.build(imageName, '.')
                    env.DOCKER_IMAGE_NAME = imageName
                }
            }
        }
        stage('Docker Desenvolvimento') {
            agent { docker { image "${env.DOCKER_IMAGE_NAME}" } }
            steps {
                script {
                    docker.image("${env.DOCKER_IMAGE_NAME}").withRun('-p 8080:8080 -d') { c -> // Inicia o container em modo detached
                        echo "Contêiner de desenvolvimento iniciado: ${c.id}"
                        echo "Acesse a aplicação em http://localhost:8080"
                    }
                }
            }
        }
    }
}
