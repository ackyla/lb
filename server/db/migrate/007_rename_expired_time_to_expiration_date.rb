class RenameExpiredTimeToExpirationDate < ActiveRecord::Migration
  def self.up
    rename_column :territories, :expired_time, :expiration_date
  end

  def self.down
    rename_column :territories, :expiration_date, :expired_time
  end
end
