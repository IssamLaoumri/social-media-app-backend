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
                sh 'mvn clean install'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn clean sonar:sonar \
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
    }
    post {
        success {
            emailext subject: "Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                     body: "The build was successful.\n\nCheck details: ${env.BUILD_URL}",
                     recipientProviders: [[$class: 'DevelopersRecipientProvider']]
        }
        failure {
            emailext subject: "Build FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                     body: "The build failed.\n\nCheck details: ${env.BUILD_URL}",
                     recipientProviders: [[$class: 'DevelopersRecipientProvider']]
        }
    }
}
