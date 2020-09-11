pipeline {
	agent none
	stages {
		stage('Build on openJDK 8') {
			agent {
				docker { image 'openjdk:8-alpine' }
			}
			steps {
				sh './gradlew build'
			}
		}
		stage('Build on openJDK 11') {
			agent {
				docker { image 'openjdk:11-alpine' }
			}
			steps {
				sh './gradlew build'
			}
		}
	}
}
