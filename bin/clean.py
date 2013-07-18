import os
import subprocess
import sys

for line in sys.stdin:
  if "[java]" in line:
    line = line.replace("     [java] ", " ")
    line = line.strip()
    print line
sys.stdout.flush()