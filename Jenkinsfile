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
                        -Dsonar.projectKey=social_media_pipeline-final \
                        -Dsonar.projectName='social_media_pipeline-final' \
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
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t ${DOCKER_IMAGE} .'
                }
            }
        }
        stage('Deploy to Test Environment') {
            steps {
                script {
                    sh 'docker stop social_media_test || true'
                    sh 'docker rm social_media_test || true'
                    sh 'docker run -d --name social_media_test -p 8080:8080 ${DOCKER_IMAGE}'
                }
            }
        }
    }
    post {
        success {
            emailext subject: "Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                     body: """<style>
                                                             body {
                                                                 margin: 0;
                                                                 padding: 0;
                                                                 font-family: 'Arial', sans-serif;
                                                                 background-color: #f5f7fa;
                                                                 color: #333;
                                                             }
                                                             .container {
                                                                 max-width: 600px;
                                                                 margin: 50px auto;
                                                                 background: #fff;
                                                                 border-radius: 12px;
                                                                 box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                                                                 overflow: hidden;
                                                             }
                                                             .header {
                                                                 padding: 20px;
                                                                 text-align: center;
                                                                 background-color: #007bff;
                                                                 color: white;
                                                             }
                                                             .header h1 {
                                                                 font-size: 24px;
                                                                 margin: 0;
                                                             }
                                                             .icon {
                                                                 font-size: 48px;
                                                                 margin-bottom: 10px;
                                                             }
                                                             .content {
                                                                 padding: 20px;
                                                             }
                                                             .status {
                                                                 display: flex;
                                                                 align-items: center;
                                                                 gap: 10px;
                                                                 margin-bottom: 20px;
                                                             }
                                                             .status.success .icon {
                                                                 color: green;
                                                             }
                                                             .status.failure .icon {
                                                                 color: red;
                                                             }
                                                             .status-text {
                                                                 font-size: 18px;
                                                                 font-weight: bold;
                                                             }
                                                             .details {
                                                                 margin-top: 10px;
                                                                 border-top: 1px solid #eee;
                                                                 padding-top: 15px;
                                                             }
                                                             .details p {
                                                                 margin: 5px 0;
                                                             }
                                                             .details a {
                                                                 color: #007bff;
                                                                 text-decoration: none;
                                                             }
                                                             .details a:hover {
                                                                 text-decoration: underline;
                                                             }
                                                             .footer {
                                                                 padding: 15px;
                                                                 text-align: center;
                                                                 background-color: #f8f9fa;
                                                                 font-size: 12px;
                                                                 color: #777;
                                                             }
                                                         </style>
                                                     </head>
                                                     <body>
                                                         <div class="container">
                                                             <!-- Header -->
                                                             <div class="header">
                                                                 <div class="icon">🚀</div>
                                                                 <h1>Build Notification</h1>
                                                             </div>

                                                             <!-- Success Content -->
                                                             <div class="content">
                                                                 <div class="status success">
                                                                     <div class="icon">✅</div>
                                                                     <div class="status-text">The build was successful!</div>
                                                                 </div>
                                                                 <div class="details">
                                                                     <p><strong>Job Name:</strong> ${env.JOB_NAME}</p>
                                                                     <p><strong>Build Number:</strong> #${env.BUILD_NUMBER}</p>
                                                                     <p><strong>Start Time:</strong> ${env.START_TIME}</p>
                                                                     <p><strong>Duration:</strong> ${env.BUILD_DURATION}</p>
                                                                     <p>
                                                                         <strong>Details:</strong>
                                                                         <a href="${env.BUILD_URL}" target="_blank">View Build Details</a>
                                                                     </p>
                                                                 </div>
                                                             </div>
                                                             <!-- Footer -->
                                                             <div class="footer">
                                                                 <p>Automated Build Notification System</p>
                                                             </div>
                                                         </div>
                                                     </body>
                                                     </html>
                           """,
                     mimeType: 'text/html',
                     to: 'issamlaoumri@gmail.com'
        }
        failure {
            emailext subject: "Build FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                     body: """<style>
                                                                     body {
                                                                         margin: 0;
                                                                         padding: 0;
                                                                         font-family: 'Arial', sans-serif;
                                                                         background-color: #f5f7fa;
                                                                         color: #333;
                                                                     }
                                                                     .container {
                                                                         max-width: 600px;
                                                                         margin: 50px auto;
                                                                         background: #fff;
                                                                         border-radius: 12px;
                                                                         box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                                                                         overflow: hidden;
                                                                     }
                                                                     .header {
                                                                         padding: 20px;
                                                                         text-align: center;
                                                                         background-color: #007bff;
                                                                         color: white;
                                                                     }
                                                                     .header h1 {
                                                                         font-size: 24px;
                                                                         margin: 0;
                                                                     }
                                                                     .icon {
                                                                         font-size: 48px;
                                                                         margin-bottom: 10px;
                                                                     }
                                                                     .content {
                                                                         padding: 20px;
                                                                     }
                                                                     .status {
                                                                         display: flex;
                                                                         align-items: center;
                                                                         gap: 10px;
                                                                         margin-bottom: 20px;
                                                                     }
                                                                     .status.success .icon {
                                                                         color: green;
                                                                     }
                                                                     .status.failure .icon {
                                                                         color: red;
                                                                     }
                                                                     .status-text {
                                                                         font-size: 18px;
                                                                         font-weight: bold;
                                                                     }
                                                                     .details {
                                                                         margin-top: 10px;
                                                                         border-top: 1px solid #eee;
                                                                         padding-top: 15px;
                                                                     }
                                                                     .details p {
                                                                         margin: 5px 0;
                                                                     }
                                                                     .details a {
                                                                         color: #007bff;
                                                                         text-decoration: none;
                                                                     }
                                                                     .details a:hover {
                                                                         text-decoration: underline;
                                                                     }
                                                                     .footer {
                                                                         padding: 15px;
                                                                         text-align: center;
                                                                         background-color: #f8f9fa;
                                                                         font-size: 12px;
                                                                         color: #777;
                                                                     }
                                                                 </style>
                                                             </head>
                                                             <body>
                                                                 <div class="container">
                                                                     <!-- Header -->
                                                                     <div class="header">
                                                                         <div class="icon">🚀</div>
                                                                         <h1>Build Notification</h1>
                                                                     </div>

                                                                     <!-- Failure Content -->
                                                                     <div class="content">
                                                                         <div class="status failure">
                                                                             <div class="icon">❌</div>
                                                                             <div class="status-text">The build failed.</div>
                                                                         </div>
                                                                         <div class="details">
                                                                             <p><strong>Job Name:</strong> ${env.JOB_NAME}</p>
                                                                             <p><strong>Build Number:</strong> #${env.BUILD_NUMBER}</p>
                                                                             <p><strong>Start Time:</strong> ${env.START_TIME}</p>
                                                                             <p><strong>Duration:</strong> ${env.BUILD_DURATION}</p>
                                                                             <p>
                                                                                 <strong>Details:</strong>
                                                                                 <a href="${env.BUILD_URL}" target="_blank">View Build Details</a>
                                                                             </p>
                                                                         </div>
                                                                     </div>

                                                                     <!-- Footer -->
                                                                     <div class="footer">
                                                                         <p>Automated Build Notification System</p>
                                                                     </div>
                                                                 </div>
                                                             </body>
                                                             </html>
                                   """,
                     mimeType: 'text/html',
                     to: 'issamlaoumri@gmail.com'
        }
    }
}
