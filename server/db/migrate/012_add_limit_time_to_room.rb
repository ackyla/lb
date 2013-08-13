class AddLimitTimeToRoom < ActiveRecord::Migration
  def self.up
    change_table :rooms do |t|
      t.integer :time_limit, default: 30
      t.datetime :termination_time
      t.remove :started
    end
  end

  def self.down
    change_table :rooms do |t|
      t.remove :limit
      t.remove :termination_time
    end
  end
end
