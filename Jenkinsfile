pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Unit Tests') {
            steps {
                sh 'mvn test -Punit-tests'
            }
        }
        stage('Integration Tests') {
            steps {
                sh 'mvn verify -Pintegration-tests'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn clean verify sonar:sonar \
                        -Dsonar.scanner.forceAnalysis=true \
                        -Dsonar.projectKey=social_media_backend_pipeline \
                        -Dsonar.projectName='social_media_backend_pipeline' \
                        -Dsonar.java.binaries=target/classes
                    """                }
            }
        }
        stage('Quality Gate') {
            steps {
                script {
                    timeout(time: 5, unit: 'MINUTES') {
                        def qualityGate = waitForQualityGate()
                        if (qualityGate.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qualityGate.status}"
                        }
                    }
                }
            }
        }
        stage('Deploy to Test Environment') {
            steps {
                echo "Deploying to Test Environment..."
                bat '''
                    if not exist C:\\test-environment mkdir C:\\test-environment
                    copy target\\*.jar C:\\test-environment\\
                    echo Application deployed to C:\\test-environment
                '''
            }
        }
    }
    post {
        success {
            emailext subject: "Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                     body: "The build was successful.\n\nCheck details: ${env.BUILD_URL}",
                     to: 'issamlaoumri@gmail.com'
        }
        failure {
            emailext subject: "Build FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                     body: "The build failed.\n\nCheck details: ${env.BUILD_URL}",
                     to: 'issamlaoumri@gmail.com'
        }
    }
}
