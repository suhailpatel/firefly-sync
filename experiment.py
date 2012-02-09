# FireflyScript.py
#
# This is a Python Script written to help us test the Experiment
# by running it repeatedly and generating useful numbers on success
# such as averages which can then be used in the report. It's
# pretty targeted towards the current implementation and will
# probably break if you change any code but it worked and served
# it's purpose for now
#
# @author Suhail

import subprocess
import time
import re

# Compile the files first...
command = ['javac', 'firefly/Firefly.java', 'firefly/FireflyInteraction.java', 'firefly/FireflyExperiment.java']
process = subprocess.Popen(command, stdout=subprocess.PIPE)
process.wait()

print ""
print "All files have been compiled! We are now ready to proceed with the experiment"
print ""

# This will hold all our successful experiments
success = []

# How many repitions do we want?
repetitions = 100

# Now repeat our FireflyExperiment 20 times
for x in range(0, repetitions):
    command = ['java', 'firefly/FireflyExperiment']
    process = subprocess.Popen(command, stdout=subprocess.PIPE)
    process.wait()
    
    # Get all the lines of our program
    lines = process.stdout.readlines()
    
    # Print out the results...
    print "Experiment: " + str(x + 1)
    print "-------------------------------------------------"
    for line in lines:
        print line[:-1]
    print "-------------------------------------------------"

    # In search for success
    search = re.search('Synchronisation achieved in (?P<value>[0-9]+) timesteps', lines[-1], re.IGNORECASE)
    if search:
        success.append(int(search.group('value')))
        print "Experiment was a success and was added to the array"
        print ""
    else:
        print "Experiment was a failure and was not added to the array"
        print ""
        
# Print out some info
print "Successful Syncronisations: " + str(len(success)) + " out of " + str(repetitions) + " repetitions"
print "Total Time Steps: " + str(sum(success))

# More information if we have any?
if len(success) > 0:
    print ""
    print "Average Time Steps: " + str(float(sum(success)) / len(success))
    print "Minimum: " + str(min(success))
    print "Maximum: " + str(max(success))
    
# Clean out the ending...    
print ""