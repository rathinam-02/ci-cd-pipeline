pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    environment {
        IMAGE_NAME = "rathinamaanikam/springboot-demo"
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

                    docker build \
                    -t ${IMAGE_NAME}:${BUILD_NUMBER} \
                    -t ${IMAGE_NAME}:latest .

                '''
            }
        }


        stage('Docker Push') {

            steps {

                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {

                    sh '''

                    echo "$DOCKER_PASS" | docker login \
                    -u "$DOCKER_USER" \
                    --password-stdin


                    docker push ${IMAGE_NAME}:${BUILD_NUMBER}

                    docker push ${IMAGE_NAME}:latest


                    docker logout

                    '''
                }
            }
        }


    }


    post {

        success {

            echo 'CI Pipeline completed successfully!'

        }


        failure {

            echo 'CI Pipeline failed!'

        }


        always {

            cleanWs()

        }

    }

}
