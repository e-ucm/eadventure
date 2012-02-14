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
    pattern = pattern or \
        '(\.java$)|(\.properties$)|(\.xml$)|(\.txt$)'
    def look(dir_name, dir_contents, callback):
        for name in dir_contents:
            source = os.path.join(dir_name, name)
            if (os.path.isdir(source)):
                look(source, os.listdir(source), callback)                     
            elif (re.search(pattern, source) != None):
                callback(source)
    look(src_dir_name, os.listdir(src_dir_name), callback)
    return

def grep_maker(pattern):
    p = pattern
    def grep(source):
        if (subprocess.call(["grep", "-n", p, source]) == 0):
            print ("=== found in " + source + " ===")
    return grep
        
def main():
    if len(sys.argv) < 2 or sys.argv[1] == '-h':
        print ("syntax: python " + sys.argv[0] + " <regex-pattern>");
        sys.exit(-1)
    
    find_and_do(".", False, grep_maker(sys.argv[1]))    
    
main()

