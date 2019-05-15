pipeline {
    agent any
    
    stages {
		stage('Payment Testsuite Execution'){

			steps{
			checkout([$class: 'GitSCM',
			  branches: [[name: '*/master']],
			  userRemoteConfigs: [
				[url: 'https://github.com/SujataKale97/new1.git',
				 credentialsId: 'ea4c3770-b2ed-4639-9ffc-cc3e586e454c']
			  ]
			])  
			
                        bat ' gradlew ' +
                      
                       "clean test -i -Ppayment"
	
			 publishHTML([reportDir: 'test-output', reportFiles: 'PaymentServiceReport.html', reportName: 'Payment Test-suite Report'])
			}
		}
	    
	}
	post {
    always {
        emailext body: 'A Test EMail', recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], subject: 'Test', to: 'kale.babanrao@happiestminds.com'
    	}
    }
}
