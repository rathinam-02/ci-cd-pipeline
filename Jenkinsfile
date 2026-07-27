pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                url: 'https://github.com/rathinam-02/ci-cd-pipeline.git'
            }
        }

        stage('Build') {
            steps {
                sh 'cd app && mvn clean package'
            }
        }

       stage('SonarQube Analysis') {
    steps {
        script {
            def scannerHome = tool 'SonarScanner'
            withSonarQubeEnv('sonarqube') {
                sh """
                    cd app
                    ${scannerHome}/bin/sonar-scanner \
                    -Dsonar.projectKey=springboot-demo \
                    -Dsonar.projectName=springboot-demo \
                    -Dsonar.sources=src \
                    -Dsonar.java.binaries=target/classes
                """
            }
        }
    }
}
