import json
import os

# from profanity_check import predict, predict_prob
# from better_profanity import profanity
# profanity.load_censor_words(whitelist_words=['god','stroke',"he'll"])

folder_in_pathname    = "/Users/sandeke/PycharmProjects/Hubitat-Fun-Api-Responses/data/raw/"
folder_out_pathname   = "/Users/sandeke/Downloads/"
filename_out      = "Idioms.json"
max_records_per_file = 101
data_out = {'Idioms': []}

def main():
    # Example usage:
    filenames_in_folder = get_filenames_in_folder(folder_in_pathname)
    print (f"Raw Json Data Folder: {folder_in_pathname}")
    if filenames_in_folder:
        for filename in filenames_in_folder:
            print(f"Processing '{filename}'")
            infile = f"{folder_in_pathname}{filename}"
            method2read(infile)
        final()

def method2read(infile):
    global data_out
    dict_raw = read_dict_from_json(infile)
    if 'Idioms' in dict_raw:
        print(infile)
        print(f"data_raw has {len(dict_raw['Idioms'])} records")
        for value in dict_raw['Idioms']:
            add2dict(value)
    else:
        print(infile)
        print(f"data_raw has {len(dict_raw)} records")
        for value in dict_raw.values():
            add2dict(value)
    print(f"data_out = {len(data_out['Idioms'])} records")

def add2dict(value):
        global data_out
        record      =  {}
        phrase      = ['title','phrase']
        definition  = ['definition', 'meaning']
        for item in phrase:
            if item in value:
                record['phrase'] = value[item].capitalize()
                break
        for item in definition:
            if item in value:
                if 'examples' in value:
                    definition = f"{value[item].capitalize()} As in this example, {','.join(value['examples'])}"
                else:

                    definition = f"Which means, {value[item][:1].lower() + value[item][1:]}"
                record['definition'] = definition
                break
        data_out['Idioms'].append(record)

def final():
    global data_out
    data_temp = {'Idioms': []}
    data_suffix = 100
    count = 0

    for index, value in enumerate(data_out['Idioms']):
        count += 1
        data_temp['Idioms'].append(value)
        if count % max_records_per_file == 0:
            filename = f"{folder_out_pathname}Idioms-{str(data_suffix)}.json"
            print(f"Writing final dictionary to to {filename} with {len(data_temp['Idioms'])} records")
            write_dict_to_json(data_temp,filename)
            data_suffix += 100
            data_temp = {'Idioms': []}
            count = 0
    filename = f"{folder_out_pathname}Idioms-{str(data_suffix)}.json"
    print (f"Writing final dictionary to to {filename} with {len(data_temp['Idioms'])} records")
    write_dict_to_json(data_temp,filename)


# Write dictionary to a JSON file
def write_dict_to_json(data, fn):
    with open(fn, 'w') as file:
        json.dump(data, file, indent=1, ensure_ascii=False)  # indent for better readability

# Read dictionary from a JSON file
def read_dict_from_json(infilename):
    try:
        with open(infilename, 'r') as file:
            data = json.load(file)
        return data
    except FileNotFoundError:
        print(f"Error: File not found at path: {infilename}")
        return None
    except json.JSONDecodeError:
        print(f"Error: Invalid JSON format in file: {infilename}")
        return None
    except IndexError:
        print(f"Error: JSON data is empty or does not contain a first object: {infilename}")
        return None
    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        return None# Example usage


def get_filenames_in_folder(folder_path):
  """
  Gets a list of filenames in the specified folder.

  Args:
    folder_path: The path to the folder.

  Returns:
    A list of strings, where each string is a filename in the folder.
    Returns an empty list if the folder does not exist or if an error occurs.
  """
  try:
    filenames = [f for f in os.listdir(folder_path) if not f.startswith('.')]
    return sorted(filenames)

  except FileNotFoundError:
    print(f"Error: Folder not found: {folder_path}")
    return []
  except Exception as e:
      print(f"An error occurred: {e}")
      return []

if __name__ == '__main__':
    main()