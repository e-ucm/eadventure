#!/usr/bin/python
# -*- coding: utf-8 -*-
import re
import os
import shutil
import sys
import subprocess

# Find files with a certain extension
#   do something with them later
def find_and_do(src_dir_name, pattern, callback):
    pattern = pattern or "R\.java$"
    def look(dir_name, dir_contents, callback):
        for name in dir_contents:
            source = os.path.join(dir_name, name)
            if (os.path.isdir(source)):
                look(source, os.listdir(source), callback)                     
            elif (re.search(pattern, source) != None):
                callback(source)
    look(src_dir_name, os.listdir(src_dir_name), callback)
    return

def main():        
    def regen(source):
        f = open(source, 'r')
        d = f.read();
        project = re.search('(.*)/src/.*', source).group(1);
        package = re.search('package (.*);', d).group(1);
        f.close()
        print ("=== processing" + 
            "\n\t" + source + 
            "\n\t" + project +
            "\n\t" + package + "===>")
        subprocess.call(["java", 
            "-cp", "core/utils/target/classes",
            "ead.utils.i18n.ResourceCreator",
            project, package, source])   

    find_and_do(".", False, regen)    
    
main()

