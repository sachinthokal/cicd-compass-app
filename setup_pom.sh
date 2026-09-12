#!/usr/bin/env bash

cat << 'EOF' > pom.xml


    4.0.0

    
        org.springframework.boot
        spring-boot-starter-parent
        3.3.3
        
    

    com.cicd
    cicd-compass-app
    1.0.0
    cicd-compass-app
    Interactive DevSecOps and CI/CD Interactive Study Guide App

    
        21
    

    
        
            org.springframework.boot
            spring-boot-starter-web
        

        
            org.springframework.boot
            spring-boot-starter-test
            test
        
    

    
        
            
                org.springframework.boot
                spring-boot-maven-plugin
            
        
    

EOF

echo "pom.xml generated successfully!"