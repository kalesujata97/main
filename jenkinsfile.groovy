node {
	stage('Payment Testsuite Execution'){
		checkout([$class: 'GitSCM',
		branches: [[name: '*/master']],
		userRemoteConfigs: [
		[url: 'https://github.com/SujataKale97/new1.git',
		credentialsId: 'ea4c3770-b2ed-4639-9ffc-cc3e586e454c']
		]])  
			
                 bat ' gradlew ' +
                      
                 "clean test -i -Ppayment"
	
		publishHTML([reportDir: 'test-output', reportFiles: 'PaymentServiceReport.html', reportName: 'Payment Test-suite Report'])
		 emailext (
			 body: 'A Test EMail', 
			 subject: 'Test', 
			)
	}
	stage('Email')
        {
            env.ForEmailPlugin = env.WORKSPACE      
            emailext attachmentsPattern: 'test-output\\*.html',      
            body: '''${SCRIPT, template="groovy_html.template"}''', 
            subject: currentBuild.currentResult + " : " + env.JOB_NAME, 
            to: 'kale.babanrao@happiestminds.com',
	    from: 'kalesujata97@gmail.com'
        }
}
