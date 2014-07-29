import java.io.*;
import java.util.*;

public class SortingTest {
	public static void main(String args[]) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			boolean isRandom = false; // 입력받은 배열이 난수인가 아닌가?
			int[] value; // 입력 받을 숫자들의 배열
			String nums = br.readLine(); // 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r') {
				// 난수일 경우
				isRandom = true; // 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]); // 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]); // 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]); // 최대값

				Random rand = new Random(); // 난수 인스턴스를 생성한다.

				value = new int[numsize]; // 배열을 생성한다.
				for (int i = 0; i < value.length; i++)
					// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			} else {
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize]; // 배열을 생성한다.
				for (int i = 0; i < value.length; i++) // 한줄씩 입력받아 배열원소로 대입
				{
					value[i] = Integer.parseInt(br.readLine());

				}
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true) {
				int[] newvalue = (int[]) value.clone(); // 원래 값의 보호를 위해 복사본을
														// 생성한다.

				// System.out.print("Sorting Type : ");

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0)) {
				case 'B': // Bubble Sort
					newvalue = DoBubbleSort(newvalue);
					break;
				case 'I': // Insertion Sort
					newvalue = DoInsertionSort(newvalue);
					break;
				case 'H': // Heap Sort
					newvalue = DoHeapSort(newvalue);
					break;
				case 'M': // Merge Sort
					newvalue = DoMergeSort(newvalue);
					break;
				case 'Q': // Quick Sort
					newvalue = DoQuickSort(newvalue);
					break;
				case 'R': // Radix Sort
					newvalue = DoRadixSort(newvalue);
					break;
				case 'X':
					return; // 프로그램을 종료한다.
				default:
					throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom) {
					// 난수일 경우 수행시간을 출력한다.
					System.out
							.println((System.currentTimeMillis() - t) + " ms");
				} else {
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++) {
						System.out.println(newvalue[i]);
					}
				}

			}
		} catch (IOException e) {
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value) {
		// TODO : Bubble Sort 를 구현하라.
		// value는 정렬안된 숫자들의 배열이며 value.length 는 배열의 크기가 된다.
		// 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
		// 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
		// 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.

		if (value.length > 1) {

			for (int i = 0; i < value.length - 1; i++) {

				boolean sortOccur = false;

				for (int j = 0; j < value.length - 1 - i; j++) {

					if (value[j] > value[j + 1]) {

						int temp = value[j + 1];
						value[j + 1] = value[j];
						value[j] = temp;

						sortOccur = true;
					}
				}

				if (sortOccur == false) {
					return value;
				}

			}
		}

		return (value);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value) {

		if (value.length > 1) {

			for (int i = 0; i < value.length - 1; i++) {

				int temp = value[i + 1];

				for (int j = i; j >= 0; j--) {

					if (value[j] > temp) {
						value[j + 1] = value[j];

						if (j == 0) {
							value[0] = temp;
							break;
						}

					} else {
						value[j + 1] = temp;
						break;
					}

				}

			}
		}

		return (value);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] value) {
		// TODO : Heap Sort 를 구현하라.

		PriorityQueue<Integer> heap = new PriorityQueue<Integer>();

		for (int i = 0; i < value.length; i++) {
			heap.offer(value[i]);
		}

		for (int j = 0; j < value.length; j++) {
			value[j] = heap.remove();
		}

		return (value);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] value) {
		if (value.length == 1) {
			return value;
		} else {

			int[] leftHalf = new int[value.length / 2];
			int[] rightHalf = new int[value.length - leftHalf.length];

			System.arraycopy(value, 0, leftHalf, 0, leftHalf.length);
			System.arraycopy(value, leftHalf.length, rightHalf, 0,
					rightHalf.length);

			leftHalf = DoMergeSort(leftHalf);
			rightHalf = DoMergeSort(rightHalf);

			int[] tempArray = new int[value.length];

			int i = 0, j = 0;

			while (true) {

				if (leftHalf[i] < rightHalf[j]) {
					tempArray[i + j] = leftHalf[i];
					i++;
				} else {
					tempArray[i + j] = rightHalf[j];
					j++;
				}

				if (i == leftHalf.length) {

					System.arraycopy(rightHalf, j, tempArray, i + j,
							rightHalf.length - j);
					break;

				} else if (j == rightHalf.length) {

					System.arraycopy(leftHalf, i, tempArray, i + j,
							leftHalf.length - i);
					break;

				}

			}

			return tempArray;
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] value) {

		if (value.length > 1) {

			int pivot = value[value.length - 1];

			int lLength = 0, rLength = 0;

			boolean sortOccur = false;

			int i, j;

			for (i = 0; i < value.length - 1; i++) {

				if (value[i] > pivot) {

					for (j = i; j < value.length - 1; j++) {

						if (value[j] < pivot) {

							int temp = value[i];
							value[i] = value[j];
							value[j] = temp;

							i++;
						}

					}

					sortOccur = true;
					break;
				}
			}

			int[] leftPartition, rightPartition;

			if (sortOccur) {

				// switch pivot item to rightside of leftPartition

				value[value.length - 1] = value[i];
				value[i] = pivot;

				lLength = i;
				rLength = value.length - i - 1;

				leftPartition = new int[lLength];
				rightPartition = new int[rLength];

				System.arraycopy(value, 0, leftPartition, 0, lLength);
				System.arraycopy(value, lLength + 1, rightPartition, 0, rLength);

				leftPartition = DoQuickSort(leftPartition);
				rightPartition = DoQuickSort(rightPartition);

			} else {

				lLength = value.length - 1;

				leftPartition = new int[lLength];
				rightPartition = new int[rLength];

				System.arraycopy(value, 0, leftPartition, 0, value.length - 1);

				leftPartition = DoQuickSort(leftPartition);

			}

			System.arraycopy(leftPartition, 0, value, 0, lLength);

			if (sortOccur) {
				System.arraycopy(rightPartition, 0, value, lLength + 1, rLength);
			}

		}

		return (value);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value) {
		ArrayList<Integer> pValueList = new ArrayList<Integer>();
		ArrayList<Integer> nValueList = new ArrayList<Integer>();

		int pMax = 0, nMin = 0;

		for (int i = 0; i < value.length; i++) {
			if (value[i] >= 0) {
				// positive number & 0 -> pValueList
				pValueList.add(value[i]);

				if (value[i] > pMax) {
					pMax = value[i];
				}

			} else {
				// negative number -> nValueList, changes to absolute value
				nValueList.add(-value[i]);

				if (value[i] < nMin) {
					nMin = value[i];
				}
			}
		}

		int pMaxSize = Integer.toString(pMax).length();
		int nMinSize = Integer.toString(-nMin).length();

		int[] pValue = new int[pValueList.size()];
		int[] nValue = new int[nValueList.size()];

		for (int i = 0; i < pValue.length; i++) {
			pValue[i] = pValueList.get(i);
		}

		for (int j = 0; j < nValue.length; j++) {
			nValue[j] = nValueList.get(j);
		}

		pValue = RadixSorting(pValue, pMaxSize, true);
		nValue = RadixSorting(nValue, nMinSize, false);

		System.arraycopy(nValue, 0, value, 0, nValue.length);
		System.arraycopy(pValue, 0, value, nValue.length, pValue.length);

		return (value);
	}

	private static int[] RadixSorting(int[] value, int maxNumSize,
			boolean isPositiveArray) {

		LinkedList<Queue<Integer>> radix = new LinkedList<Queue<Integer>>();

		for (int i = 0; i < 10; i++) {
			radix.add(new LinkedList<Integer>());
		}

		// loop of each decimal
		int divider = 1;
		for (int i = 1; i <= maxNumSize; i++) {

			// enQueue each value element to the radix list by its radix number
			for (int j = 0; j < value.length; j++) {

				int jThDigit = (value[j] / divider) % 10;
				radix.get(jThDigit).add(value[j]);

			}

			divider *= 10;

			// deQueue from the radix list to the value array
			int j = 0;
			for (int k = 0; k < 10; k++) {

				while (!radix.get(k).isEmpty()) {
					value[j] = radix.get(k).poll();
					j++;
				}
			}

		}

		if (!isPositiveArray) {
			int[] reverseValue = new int[value.length];
			for (int i = 0; i < value.length; i++) {
				reverseValue[i] = -value[value.length - i - 1];
			}
			return reverseValue;

		}

		return value;

	}

}
