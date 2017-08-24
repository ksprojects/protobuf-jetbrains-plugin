pipeline {
    agent any
    tools {
        jdk 'JDK 8'
    }
    options {
        ansiColor('xterm')
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }
    stages {
        stage('Build') {
            steps {
                sh '''
                    sh ./gradlew clean build                                                           
                '''
            }
            post {
                always {
                    junit '**/build/test-results/**/*.xml'
                }
            }
        }
    }
    post {
        failure {
            emailext(
                    mimeType: 'text/html',
                    body: '${JELLY_SCRIPT,template="html"}',
                    recipientProviders: [
                            [$class: 'CulpritsRecipientProvider'],
                            [$class: 'DevelopersRecipientProvider'],
                            [$class: 'RequesterRecipientProvider']
                    ],
                    subject: 'Build failed - ${JOB_NAME}'
            )
        }
        unstable {
            emailext(
                    mimeType: 'text/html',
                    body: '${JELLY_SCRIPT,template="html"}',
                    recipientProviders: [
                            [$class: 'CulpritsRecipientProvider'],
                            [$class: 'DevelopersRecipientProvider'],
                            [$class: 'RequesterRecipientProvider']
                    ],
                    subject: 'Build unstable - ${JOB_NAME}'
            )
        }
    }
}