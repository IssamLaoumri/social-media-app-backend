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
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn clean verify sonar:sonar \
                        -Dsonar.webhooks.project=http://127.0.0.1:8085/sonarqube-webhook/ \
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
}
