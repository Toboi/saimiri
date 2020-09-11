pipeline {
	agent none
	stages {
        stage('Build all platforms') {
            matrix {
                axes {
                    axis {
                        name 'JDK_VERSION'
                        values '8', '11'
                    }
                }
                stages {
                    stage('Build') {
                        agent {
                            docker { image 'openjdk:${JDK_VERSION}' }
                        }
                        steps {
                            sh './gradlew --no-daemon build'
                        }
                    }
                }
            }
        }
	}
}
