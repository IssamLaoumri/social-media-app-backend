pipeline {
    agent any
    environment {
        SONAR_TOKEN = credentials('SONARQUBE_TOKEN_ID') // Jenkins credentials ID for Sonar token
    }
    tools {
        maven 'maven' // Name of the Maven installation in Jenkins
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
                    sh "clean verify sonar:sonar -Dsonar.projectKey=social_media_backend_pipepline -Dsonar.projectName='social_media_backend_pipepline'"
                }
            }
        }
        stage('Quality Gate') {
            steps {
                script {
                    timeout(time: 1, unit: 'MINUTES') {
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
