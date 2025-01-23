package Alogrithms;

import Models.JobApplication;
import MyCollections.AppArray;
import MyCollections.AppBag;

public class AppQuickSort {
	public static void quickSort(AppBag<JobApplication> arr, String sortBy) {
        JobApplication[] jobArray = toArray(arr);
        quickSort(jobArray, 0, jobArray.length - 1, sortBy);
        arr.clear();
        for (JobApplication job : jobArray) {
            arr.add(job);
        }
    }

    private static void quickSort(JobApplication[] arr, int low, int high, String sortBy) {
        if (low < high) {
            int pi = partition(arr, low, high, sortBy);

            quickSort(arr, low, pi - 1, sortBy);
            quickSort(arr, pi + 1, high, sortBy);
        }
    }

    private static int partition(JobApplication[] arr, int low, int high, String sortBy) {
        JobApplication pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (compare(arr[j], pivot, sortBy) < 0) {
                i++;

                JobApplication temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        JobApplication temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    private static int compare(JobApplication job1, JobApplication job2, String sortBy) {
        switch (sortBy) {
            case "appliedDate":
                return job1.getAppliedDate().compareTo(job2.getAppliedDate());
            case "companyName":
                return job1.getCompanyName().compareTo(job2.getCompanyName());
            case "jobTitle":
                return job1.getJobTitle().compareTo(job2.getJobTitle());
            default:
                throw new IllegalArgumentException("Invalid sort criteria: " + sortBy);
        }
    }

    private static JobApplication[] toArray(AppBag<JobApplication> arr) {
        JobApplication[] result = new JobApplication[arr.getCurrentSize()];
        for (int i = 0; i < arr.getCurrentSize(); i++) {
            result[i] = arr.get(i);
        }
        return result;
    }
}
