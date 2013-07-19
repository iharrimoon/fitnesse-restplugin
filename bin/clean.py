import os
import subprocess
import sys
import re

str = ""

for line in sys.stdin:
  if "[java]" in line:
    line = line.replace("     [java] ", " ")
    line = line.strip()
    if line[0] == ".":
      line = line.replace(".", "TEST PASS - ", 1)
    if line[0] == "F":
      line = line.replace("F", " 500 FAIL - ", 1)
    if line[0] == "X":
      line = line.replace("X", " 404 FAIL - ", 1)
    line = line.repalace("FrontPage.TestRoot.TestingSuite.", "...", 1)
    re.(r"R:\d+    ", "", line)
    re.(r"E:\d+    ", "", line)
    re.(r"I:\d+    ", "", line)
      
    
sys.stdout.flush()