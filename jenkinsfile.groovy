node {
	stage('Payment Testsuite Execution'){
		checkout([$class: 'GitSCM',
		branches: [[name: '*/master']],
		userRemoteConfigs: [
		[url: 'https://github.com/SujataKale97/new1.git',
		credentialsId: 'ea4c3770-b2ed-4639-9ffc-cc3e586e454c']
		]])  
			
                 bat ' gradlew ' +
                      
                 "clean test -Ppayment"
	
		publishHTML([reportDir: 'test-output', reportFiles: 'PaymentServiceReport.html', reportName: 'Payment Test-suite Report'])
	}
	stage('Email')
        {
            env.ForEmailPlugin = env.WORKSPACE      
            emailext ( attachmentsPattern: "test-output/PaymentServiceReport.html", 
            body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}", 
            subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
	recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']]
            
	     )
        }
}
