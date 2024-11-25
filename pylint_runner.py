import sys
from pylint import epylint as lint

def run_pylint(file_path):
    (pylint_stdout, _) = lint.py_run(file_path, return_std=True)
    print(pylint_stdout.read())

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python pylint_runner.py <file_path>")
        sys.exit(1)
    
    file_path = sys.argv[1]
    run_pylint(file_path)
