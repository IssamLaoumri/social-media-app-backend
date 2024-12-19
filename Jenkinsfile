pipeline {
    agent any
    tools {
        maven 'maven'
    }
    environment {
        DOCKER_IMAGE = "social_media_backend_pipeline:${env.BUILD_NUMBER}"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
//         stage('Unit Tests') {
//             steps {
//                 sh 'mvn test -Punit-tests'
//             }
//         }
//         stage('Integration Tests') {
//             steps {
//                 sh 'mvn verify -Pintegration-tests'
//             }
//         }
//         stage('Build') {
//             steps {
//                 sh 'mvn clean install -DskipTests'
//             }
//         }
//         stage('SonarQube Analysis') {
//             steps {
//                 withSonarQubeEnv('SonarQube') {
//                     sh """
//                         mvn clean verify sonar:sonar \
//                         -Dsonar.scanner.forceAnalysis=true \
//                         -Dsonar.projectKey=social_media_pipeline-final \
//                         -Dsonar.projectName='social_media_pipeline-final' \
//                         -Dsonar.java.binaries=target/classes
//                     """                }
//             }
//         }
//         stage('Quality Gate') {
//             steps {
//                 script {
//                     timeout(time: 5, unit: 'MINUTES') {
//                         def qualityGate = waitForQualityGate()
//                         if (qualityGate.status != 'OK') {
//                             error "Pipeline aborted due to quality gate failure: ${qualityGate.status}"
//                         }
//                     }
//                 }
//             }
//         }
//         stage('Build Docker Image') {
//             steps {
//                 script {
//                     sh 'docker build -t ${DOCKER_IMAGE} .'
//                 }
//             }
//         }
//         stage('Deploy to Test Environment') {
//             steps {
//                 script {
//                     sh 'docker stop social_media_test || true'
//                     sh 'docker rm social_media_test || true'
//                     sh 'docker run -d --name social_media_test -p 8080:8080 ${DOCKER_IMAGE}'
//                 }
//             }
//         }
    }
    post {
        success {
            script{
                def emailTemplate = readFile('email.html')
                def emailBody = emailTemplate.replace('${JOB_NAME}', env.JOB_NAME)
                                             .replace('${BUILD_NUMBER}', env.BUILD_NUMBER)
                                             .replace('${status}', "Successful")
//                                              .replace('${BUILD_URL}', env.BUILD_URL)
//                                              .replace('${BUILD_DURATION}', currentBuild.durationString)
//                                              .replace('${BUILD_CAUSE}', currentBuild.getBuildCauses()[0].shortDescription)

                emailext subject: "Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                         body: emailBody,
                         to: 'issamlaoumri@gmail.com'
            }
        }
        failure {
            script{
                def emailTemplate = readFile('email.html')
                def emailBody = emailTemplate.replace('${JOB_NAME}', env.JOB_NAME)
                                             .replace('${BUILD_NUMBER}', env.BUILD_NUMBER)
                                             .replace('${status}', "Failed")
                //                                              .replace('${BUILD_URL}', env.BUILD_URL)
                //                                              .replace('${BUILD_DURATION}', currentBuild.durationString)
                //                                              .replace('${BUILD_CAUSE}', currentBuild.getBuildCauses()[0].shortDescription)

                emailext subject: "Build FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                         body: emailBody,
                         to: 'issamlaoumri@gmail.com'
            }
        }
    }
}
