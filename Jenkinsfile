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
                sh '''
                    cd app
                    mvn clean package
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                        cd app
                        /opt/sonar-scanner-5.0.1.3006-linux/bin/sonar-scanner \
                        -Dsonar.projectKey=springboot-demo \
                        -Dsonar.projectName=springboot-demo \
                        -Dsonar.sources=src \
                        -Dsonar.java.binaries=target/classes
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    cd app
                    docker build -t rathinam02/springboot-demo:${BUILD_NUMBER} .
                    docker tag rathinam02/springboot-demo:${BUILD_NUMBER} rathinam02/springboot-demo:latest
                '''
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'USER',
                        passwordVariable: 'PASS'
                    )
                ]) {
                    sh '''
                        echo "$PASS" | docker login -u "$USER" --password-stdin
                        docker push rathinam02/springboot-demo:${BUILD_NUMBER}
                        docker push rathinam02/springboot-demo:latest
                        docker logout
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed.'
        }

        always {
            cleanWs()
        }
    }
}
