/**
 * Formats a number using the Indian numbering system (e.g., 1,00,000)
 * @param {number|string} number - The number to format
 * @returns {string} - The formatted number string
 */
export function formatNumber(number) {
  // Handle invalid inputs
  if (number === null || number === undefined || Number.isNaN(Number(number))) {
    return '0';
  }
  
  const num = Number(number);
  
  // Use Intl.NumberFormat with 'en-IN' locale for Indian numbering system
  return new Intl.NumberFormat('en-IN').format(num);
}
